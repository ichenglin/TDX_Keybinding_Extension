package net.ichenglin.kbext.extension;

import net.ichenglin.kbext.object.GameState;
import net.ichenglin.kbext.object.RecognitionException;
import net.ichenglin.kbext.object.ValueRange;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import net.sourceforge.tess4j.util.LoadLibs;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class KeybindingRecognition {

    private final Tesseract     recognition_instance;
    private       BufferedImage recognition_image;
    private       int           recognition_width;
    private       int           recognition_height;

    private static final int BACKGROUND_SCOREBOARD      = 0xFF191919;
    private static final int BACKGROUND_VOTESKIP_ACCEPT = 0xFF90CC00;
    private static final int BACKGROUND_VOTESKIP_REJECT = 0xFFB42B2B;

    public KeybindingRecognition() {
        this.recognition_instance = new Tesseract();
        // initialize tesseract
        this.recognition_instance.setDatapath(LoadLibs.extractTessResources("tessdata").getAbsolutePath());
        this.recognition_instance.setLanguage("eng");
        this.recognition_instance.setTessVariable("user_defined_dpi", "70");
    }

    public String recognize_text(Rectangle image_bounds, String image_characters) throws RecognitionException {
        try {
            this.recognition_instance.setTessVariable("tessedit_char_whitelist", image_characters);
            BufferedImage image_cropped  = this.recognition_image.getSubimage(image_bounds.x, image_bounds.y, image_bounds.width, image_bounds.height);
            BufferedImage image_filtered = KeybindingRecognition.transform_monochrome(image_cropped);
            return this.recognition_instance.doOCR(image_filtered);
        } catch (TesseractException exception) {
            throw new RecognitionException("err_txt");
        }
    }

    public GameState recognize_state() throws RecognitionException {
        Rectangle scoreboard_rectangle  = this.locate_scoreboard();
        String    image_text            = this.recognize_text(scoreboard_rectangle, "0123456789:");
        String[]  image_text_parameters = image_text.split("\n");
        String round_health, round_wave, round_timer;
        round_health = round_wave = round_timer = "00";
        switch (image_text_parameters.length) {
            case 1:
                round_health = image_text_parameters[0];
                break;
            case 2:
                round_health = image_text_parameters[0];
                round_timer  = image_text_parameters[1];
                break;
            case 3:
                round_health = image_text_parameters[0];
                round_wave   = image_text_parameters[1];
                round_timer  = image_text_parameters[2];
                break;
        }
        try {
            return new GameState(
                    Integer.parseInt(round_health),
                    Integer.parseInt(round_wave),
                    KeybindingRecognition.parse_time(round_timer)
            );
        } catch (NumberFormatException exception) {
            throw new RecognitionException("err_ste");
        }
    }

    public Rectangle locate_scoreboard() throws RecognitionException {
        int recognition_width_center = (this.recognition_width  / 2);
        int recognition_height_limit = (this.recognition_height / 2);
        HashMap<Integer, NeighborOccurrence> neighbor_occurrences = new HashMap<Integer, NeighborOccurrence>();
        NeighborOccurrence occurrence_best = new NeighborOccurrence(0);
        for (int pixel_y = 0; pixel_y < recognition_height_limit; pixel_y++) {
            int pixel_argb = this.recognition_image.getRGB(recognition_width_center, pixel_y);
            if (pixel_argb != KeybindingRecognition.BACKGROUND_SCOREBOARD) continue;
            int pixel_neighbors = this.locate_scoreboard_neighbors(recognition_width_center, pixel_y);
            neighbor_occurrences.putIfAbsent(pixel_neighbors, new NeighborOccurrence(pixel_neighbors));
            NeighborOccurrence neighbor_occurrence = neighbor_occurrences.get(pixel_neighbors);
            neighbor_occurrence.add_occurrence(pixel_y);
            if (neighbor_occurrence.compareTo(occurrence_best) > 0) occurrence_best = neighbor_occurrence;
        }
        ArrayList<Integer> occurrence_list = occurrence_best.get_occurrence();
        if (occurrence_list.isEmpty()) throw new RecognitionException("err_loc");
        int scoreboard_width  = occurrence_best.get_length();
        int scoreboard_height = occurrence_list.get(occurrence_list.size() - 1);
        int scoreboard_x      = (recognition_width_center - (scoreboard_width / 2));
        if (scoreboard_width  <= 0) scoreboard_width  = 1;
        if (scoreboard_height <= 0) scoreboard_height = 1;
        return new Rectangle(scoreboard_x, 0, scoreboard_width, scoreboard_height);
    }

    public Point[] locate_skipwave(Point window_location) throws RecognitionException {
        Rectangle  scoreboard_rectangle     = this.locate_scoreboard();
        int        recognition_width_center = (this.recognition_width  / 2);
        int        recognition_height_limit = (this.recognition_height / 2);
        int        skip_offset_x            = (int) (scoreboard_rectangle.getWidth() * 0.55f);
        int        skip_reject_x            = (recognition_width_center - skip_offset_x);
        int        skip_accept_x            = (recognition_width_center + skip_offset_x);
        ValueRange range_best               = null;
        ValueRange range_current            = null;
        for (int pixel_y = 0; pixel_y < recognition_height_limit; pixel_y++) {
            if (this.recognition_image.getRGB(skip_reject_x, pixel_y) != KeybindingRecognition.BACKGROUND_VOTESKIP_REJECT) continue;
            if (this.recognition_image.getRGB(skip_accept_x, pixel_y) != KeybindingRecognition.BACKGROUND_VOTESKIP_ACCEPT) continue;
            boolean current_continuous = (range_current != null) && ((range_current.get_maximum() - pixel_y) <= 1);
            if (current_continuous) range_current.set_maximum(pixel_y);
            else                    range_current = new ValueRange(pixel_y, pixel_y);
            boolean current_greater = (range_best == null) || (range_best.get_length() < range_current.get_length());
            if (current_greater) range_best = range_current.get_copy();
        }
        if (range_best == null) throw new RecognitionException("err_loc");
        int skip_y = (int) range_best.get_mean();
        return new Point[] {
            new Point((window_location.x + skip_reject_x), (window_location.y + skip_y)),
            new Point((window_location.x + skip_accept_x), (window_location.y + skip_y)),
        };
    }

    public void set_image(BufferedImage recognition_image) {
        this.recognition_image    = recognition_image;
        this.recognition_width    = recognition_image.getWidth();
        this.recognition_height   = recognition_image.getHeight();
    }

    private int locate_scoreboard_neighbors(int pixel_x, int pixel_y) {
        int pixel_neighbors = 1;
        for (int neighbor_x = (pixel_x-1); neighbor_x >= 0; neighbor_x--) {
            int pixel_argb = this.recognition_image.getRGB(neighbor_x, pixel_y);
            if (pixel_argb != KeybindingRecognition.BACKGROUND_SCOREBOARD) break;
            pixel_neighbors++;
        }
        for (int neighbor_x = (pixel_x+1); neighbor_x < this.recognition_width; neighbor_x++) {
            int pixel_argb = this.recognition_image.getRGB(neighbor_x, pixel_y);
            if (pixel_argb != KeybindingRecognition.BACKGROUND_SCOREBOARD) break;
            pixel_neighbors++;
        }
        return pixel_neighbors;
    }

    public static BufferedImage transform_monochrome(BufferedImage image_original) {
        // convert to black and white
        BufferedImage image_destination = new BufferedImage(image_original.getWidth(), image_original.getHeight(), BufferedImage.TYPE_BYTE_BINARY);
        Graphics2D    image_graphics    = image_destination.createGraphics();
        image_graphics.drawImage(image_original, 0 ,0, Color.WHITE, null);
        image_graphics.dispose();
        // invert colors
        int image_width  = image_destination.getWidth();
        int image_height = image_destination.getHeight();
        for (int pixel_x = 0; pixel_x < image_width; pixel_x++) {
            for (int pixel_y = 0; pixel_y < image_height; pixel_y++) {
                image_destination.setRGB(pixel_x, pixel_y, (0xFFFFFF - image_destination.getRGB(pixel_x, pixel_y)));
            }
        }
        return image_destination;
    }

    private static int parse_time(String time_text) {
        Matcher time_matcher = Pattern.compile("^(\\d{1,2})?:?(\\d{2})$").matcher(time_text);
        if (!time_matcher.find()) return 0;
        try {
            int time_minutes = ((time_matcher.group(1) != null) ? Integer.parseInt(time_matcher.group(1)) : 0);
            int time_seconds = ((time_matcher.group(2) != null) ? Integer.parseInt(time_matcher.group(2)) : 0);
            return ((60 * time_minutes) + time_seconds);
        } catch (NumberFormatException exception) {
            return 0;
        }
    }
}

class NeighborOccurrence implements Comparable<NeighborOccurrence> {

    private final int                occurrence_length;
    private final ArrayList<Integer> occurrence_y;

    public NeighborOccurrence(int occurrence_length) {
        this.occurrence_length = occurrence_length;
        this.occurrence_y      = new ArrayList<Integer>();
    }

    public void add_occurrence(int occurrence_y) {
        this.occurrence_y.add(occurrence_y);
    }

    public ArrayList<Integer> get_occurrence() {
        return this.occurrence_y;
    }

    public int get_size() {
        return this.occurrence_y.size();
    }

    public int get_length() {
        return this.occurrence_length;
    }

    @Override
    public int compareTo(NeighborOccurrence object) {
        return (this.get_size() - object.get_size());
    }
}