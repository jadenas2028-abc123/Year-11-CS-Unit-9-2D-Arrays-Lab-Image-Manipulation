package code;

import image.Pixel;
import image.APImage;

public class ImageManipulation {

    /** CHALLENGE 0: Display Image
     *  Write a statement that will display the image in a window
     */
    public static void main(String[] args) {
        // --- default APImage ---
        //APImage img = new APImage("cyberpunk2077.jpg");
        //System.out.println(img);
        //img.draw();

        // --- grayScale ---
        //grayScale("cyberpunk2077.jpg");

        // --- blackAndWhite ---
        //blackAndWhite("cyberpunk2077.jpg");

        // --- edgeDetection ---
        //edgeDetection("cyberpunk2077.jpg", 20);

        // --- reflectImage ---
        //reflectImage("cyberpunk2077.jpg");

        // ---rotateImage --
        //rotateImage("cyberpunk2077.jpg");

        // --- Personal manipulation --- //
        blueTintReflect("cyberpunk2077.jpg");
    }

    /** CHALLENGE ONE: Grayscale
     *
     * INPUT: the complete path file name of the image
     * OUTPUT: a grayscale copy of the image
     *
     * To convert a colour image to grayscale, we need to visit every pixel in the image ...
     * Calculate the average of the red, green, and blue components of the pixel.
     * Set the red, green, and blue components to this average value. */
    public static void grayScale(String pathOfFile) { //did some checking, and "path of file" basically refers to the image you are using

        APImage img = new APImage(pathOfFile); // importing the image "cyberpunk2077.jpg"
        int width = img.getWidth();
        int height = img.getHeight();

        for (int x = 0; x < width; x++) { //2D array style, scanning pixel by pixel
            for (int y = 0; y < height; y++) {
                Pixel p = img.getPixel(x, y);  // "give me pixel p at (x,y), where RGB values are going to be stored in p"

                int avg = getAverageColour(p); //calling helper variable

                p.setRed(avg);
                p.setGreen(avg);
                p.setBlue(avg);
            }
        }
        img.draw(); // equivalent of printing the image
    }


    /** A helper method that can be used to assist you in each challenge.
     * This method simply calculates the average of the RGB values of a single pixel.
     * @param pixel
     * @return the average RGB value
     */
    private static int getAverageColour(Pixel pixel) {
        int r = pixel.getRed();
        int g = pixel.getGreen();
        int b = pixel.getBlue();

        int avg = (r + g + b) / 3;
        return avg;
    }

    /** CHALLENGE TWO: Black and White
     *
     * INPUT: the complete path file name of the image
     * OUTPUT: a black and white copy of the image
     *
     * To convert a colour image to black and white, we need to visit every pixel in the image ...
     * Calculate the average of the red, green, and blue components of the pixel.
     * If the average is less than 128, set the pixel to black
     * If the average is equal to or greater than 128, set the pixel to white */
    public static void blackAndWhite(String pathOfFile) {
        APImage img = new APImage(pathOfFile); //extremely similar logic to previous challenge, includes average
        int width = img.getWidth();
        int height = img.getHeight();

        for (int x = 0; x < width; x++) { //2D array style, scanning pixel by pixel
            for (int y = 0; y < height; y++) {
                Pixel p = img.getPixel(x, y); // "give me pixel p at (x,y), where RGB values are going to be stored in p"

                int avg = getAverageColour(p); //calling helper variable ABOVE ALREADY USED!

                if (avg < 128) { //if the average of the colors is dark, it will convert to black (0,0,0)
                    // dark --> black
                    p.setRed(0);
                    p.setGreen(0);
                    p.setBlue(0);
                }
                else { //if the average of the colors is bright, it will convert to white (max,max,max)
                    // bright --> white
                    p.setRed(255);
                    p.setGreen(255);
                    p.setBlue(255);
                }
            }
        }
        img.draw(); //equivalent to printing
    }

    /** CHALLENGE Three: Edge Detection
     *
     * INPUT: the complete path file name of the image
     * OUTPUT: an outline of the image. The amount of information will correspond to the threshold.
     *
     * Edge detection is an image processing technique for finding the boundaries of objects within images.
     * It works by detecting discontinuities in brightness. Edge detection is used for image segmentation
     * and data extraction in areas such as image processing, computer vision, and machine vision.
     *
     * There are many different edge detection algorithms. We will use a basic edge detection technique
     * For each pixel, we will calculate ...
     * 1. The average colour value of the current pixel
     * 2. The average colour value of the pixel to the left of the current pixel
     * 3. The average colour value of the pixel below the current pixel
     * If the difference between 1. and 2. OR if the difference between 1. and 3. is greater than some threshold value,
     * we will set the current pixel to black. This is because an absolute difference that is greater than our threshold
     * value should indicate an edge and thus, we colour the pixel black.
     * Otherwise, we will set the current pixel to white
     * NOTE: We want to be able to apply edge detection using various thresholds
     * For example, we could apply edge detection to an image using a threshold of 20 OR we could apply
     * edge detection to an image using a threshold of 35
     *  */

    public static void edgeDetection(String pathToFile, int threshold) {
        APImage img = new APImage(pathToFile);
        int width = img.getWidth();
        int height = img.getHeight();

        // I need to create a new imageFile as output since we will be referencing the
        // original imageFile for comparing neighbouring pixels. I realised that modifying
        // the original imageFile while reading from it would corrupt the comparisons
        APImage newImage = img.clone();
        Pixel black = new Pixel(0,0,0);         // Helper object for setting black pixel for setPixel()
        Pixel white = new Pixel(255,255,255);   // Helper object for setting white pixel for setPixel()

        for (int x = 0; x < width; x++){
            for (int y = 0; y < height; y++){
                Pixel p1 = img.getPixel(x,y);                   // Use Pixel class to point to (x,y) of imageFile
                int averageCurrentRGB = getAverageColour(p1);   // Get average of the RGB values of current pixel

                if(x == 0){             // There is no pixel to the left when x == 0
                    if(y != height-1) { // There is no "pixel below the current pixel" when height is reached
                        Pixel p2 = img.getPixel(x,y+1);             // Use Pixel class to point to below pixel
                        int averageBelowRGB = getAverageColour(p2); // Get average of the RGB values of below pixel

                        int diffAvg = Math.abs(averageCurrentRGB - averageBelowRGB); // Calculate abs diff in two averages

                        if (diffAvg > threshold){   // If difference > threshold then set to black
                            newImage.setPixel(x, y, black);
                        } else {                    // else set to white
                            newImage.setPixel(x, y, white);
                        }
                    } else {    // When x==0, y==height as there is no below pixel for comparison
                        newImage.setPixel(x, y, white); // Set x==0, y==height to white for no edge
                    }
                } else {    // x>0, y up to height
                    if(y != height-1) {   // There is no "pixel below the current pixel" when height is reached
                        Pixel p2 = img.getPixel(x,y+1);                 // Use Pixel class to point to below pixel
                        Pixel p3 = img.getPixel(x-1,y);                 // Use Pixel class to point to left pixel
                        int averageBelowRGB = getAverageColour(p2);     // Get average of the RGB values of below pixel
                        int averageLeftRGB = getAverageColour(p3);      // Get average of the RGB values of left pixel

                        int diffAvgBC = Math.abs(averageCurrentRGB - averageBelowRGB);  // Calculate abs diff Current - Below
                        int diffAvgLC = Math.abs(averageCurrentRGB - averageLeftRGB);   // Calculate abs diff Current - Left

                        if ((diffAvgBC > threshold) || (diffAvgLC > threshold)){    // If any diff > threshold then set to black
                            newImage.setPixel(x, y, black);
                        } else {                                                    // else set to white
                            newImage.setPixel(x, y, white);
                        }
                    } else { // When y==height, then just compare x-1
                        Pixel p3 = img.getPixel(x-1,y);                 // Use Pixel class to point to left pixel
                        int averageLeftRGB = getAverageColour(p3);      // Get average of the RGB values of left pixel

                        int diffAvgLC = Math.abs(averageCurrentRGB - averageLeftRGB);   // Calculate abs diff Current - Left

                        if (diffAvgLC > threshold){     // If difference > threshold then set to black
                            newImage.setPixel(x, y, black);
                        } else {                        // else set to white
                            newImage.setPixel(x, y, white);
                        }
                    }
                }
            }
        }
        newImage.draw();
    }

    /** CHALLENGE Four: Reflect Image
     *
     * INPUT: the complete path file name of the image
     * OUTPUT: the image reflected about the y-axis
     *
     */
    public static void reflectImage(String pathToFile) {
        APImage image = new APImage(pathToFile);    // importing image, same syntax as before.
        int width  = image.getWidth();              // get the width and height
        int height = image.getHeight();
        int halfWidth = width / 2;  // only go HALFWAY for x (using integer division "/")

        for (int y = 0; y < height; y++) {          // for every column y
            for (int x = 0; x < halfWidth; x++) {   // for every row x

                // finds the horizontal symmetric partner of the specific pixel
                int mirrorX = width - 1 - x; // column index of the mirror position on the right

                Pixel leftPixel  = image.getPixel(x, y); // getting the 2 pixels you want to swap
                Pixel rightPixel = image.getPixel(mirrorX, y);

                int leftRed   = leftPixel.getRed();
                int leftGreen = leftPixel.getGreen();
                int leftBlue  = leftPixel.getBlue();

                int rightRed   = rightPixel.getRed();
                int rightGreen = rightPixel.getGreen();
                int rightBlue  = rightPixel.getBlue();

                leftPixel.setRed(rightRed);
                leftPixel.setGreen(rightGreen);
                leftPixel.setBlue(rightBlue);

                rightPixel.setRed(leftRed);
                rightPixel.setGreen(leftGreen);
                rightPixel.setBlue(leftBlue);
            }
        }
        image.draw();
    }

    /** CHALLENGE Five: Rotate Image
     *
     * INPUT: the complete path file name of the image
     * OUTPUT: the image rotated 90 degrees CLOCKWISE
     *
     *  */
    public static void rotateImage(String pathToFile) {
        APImage original = new APImage(pathToFile);
        int width  = original.getWidth(); //finding ORIGINAL dimensions, used to put into NEW dimensions
        int height = original.getHeight();

        // create new image with specific dimensions
        // (APImage(int width, int height) creates a black image). Rotate by swapping width with height.
        APImage rotated = new APImage(height, width);

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {

                Pixel origPixel = original.getPixel(x, y); //Getting original pixel

                // Copy over the colour (R,G,B) from original to rotated
                int originalRed   = origPixel.getRed();
                int originalGreen = origPixel.getGreen();
                int originalBlue  = origPixel.getBlue();

                int newX = height - 1 - y; //new coordinates in rotated image!
                int newY = x;

                Pixel rotPixel = rotated.getPixel(newX, newY);

                rotPixel.setRed(originalRed);
                rotPixel.setGreen(originalGreen);
                rotPixel.setBlue(originalBlue);
            }
        }
        rotated.draw();
    }




    //----------------------------------------------------------------------------------------------------------------
    //----------------------------------------------------------------------------------------------------------------
    //---------------------------------------My own manipulation Crit B. ---------------------------------------------



    public static void blueTintReflect(String pathToFile) {
        APImage image = new APImage(pathToFile);

        int imageWidth  = image.getWidth();
        int imageHeight = image.getHeight();
        int halfWidth = imageWidth / 2;

        for (int y = 0; y < imageHeight; y++) {
            for (int x = 0; x < imageWidth; x++) {

                Pixel pixel = image.getPixel(x, y);

                int red   = pixel.getRed();
                int green = pixel.getGreen();
                int blue  = pixel.getBlue();

                // Increase blue, but keep it at most 255
                int extraBlue = 80; // strength of tint
                int newBlue = blue + extraBlue;
                if (newBlue > 255) {
                    newBlue = 255;
                }

                pixel.setRed(red);       // keep red the same
                pixel.setGreen(green);   // keep green the same
                pixel.setBlue(newBlue);  // set the boosted blue
            }
        }

        // Reflect the tinted image about the y-axis
        for (int y = 0; y < imageHeight; y++) {          // for every row (y)
            for (int x = 0; x < halfWidth; x++) {        // only left half of the row (x)

                int mirrorX = imageWidth - 1 - x;        // matching column on the right

                Pixel leftPixel  = image.getPixel(x,       y);
                Pixel rightPixel = image.getPixel(mirrorX, y);

                int leftRed   = leftPixel.getRed();
                int leftGreen = leftPixel.getGreen();
                int leftBlue  = leftPixel.getBlue();

                int rightRed   = rightPixel.getRed();
                int rightGreen = rightPixel.getGreen();
                int rightBlue  = rightPixel.getBlue();

                leftPixel.setRed(rightRed);
                leftPixel.setGreen(rightGreen);
                leftPixel.setBlue(rightBlue);

                rightPixel.setRed(leftRed);
                rightPixel.setGreen(leftGreen);
                rightPixel.setBlue(leftBlue);
            }
        }


        image.draw();
    }

}
