package gaf.util;

import com.groupdocs.viewer.config.ViewerConfig;
import com.groupdocs.viewer.domain.image.PageImage;
import com.groupdocs.viewer.handler.ViewerImageHandler;
import com.groupdocs.viewer.licensing.License;

import javax.imageio.ImageIO;
import java.io.*;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.List;
import java.util.Properties;

public class Utilities {

    private static final Path storagePath = getProjectBaseDir().resolve("attachs");
    private static final Path tempPath = FileSystems.getDefault().getPath(System.getProperty("java.io.tmpdir"));
    private static final Path licensePath = getProjectBaseDir().resolve("GroupDocs.Total.Java.lic");
    //Generated html files will be saved in Html folder with name starting with output_
    private static final Path outputHtmlPath = getProjectBaseDir().resolve("attachs/Output/Html");
    //Generated image files will be saved in Images folder with name starting with output_
    private static final Path outputImagePath = getProjectBaseDir().resolve("attachs/Output/Images");
    //Generated files will be saved in Output folder with name starting with output_
    private static final Path outputPath = getProjectBaseDir().resolve("attachs/Output");

    /**
     * This method applies product license from file
     *
     */
    public static void applyLicenseFromFile() {
        try {
            // Setup license
            License lic = new License();
            lic.setLicense(licensePath.toString());
        } catch (Exception exp) {
            System.out.println("Exception: " + exp.getMessage());
            exp.printStackTrace();
        }
    }

    private static Path getProjectBaseDir() {
        Properties props = new Properties();
        try {
            InputStream i = Utilities.class.getResourceAsStream("/project.properties");
            props.load(i);
        } catch (IOException x) {
            throw new RuntimeException(x);
        }
        return FileSystems.getDefault().getPath(props.getProperty("project.basedir"));
    }

    /**
     * This method applies product license from stream
     *
     */
    public static void applyLicenseFromStream(String filePath) {
        try {
            // Obtain license stream
            FileInputStream licenseStream = new FileInputStream(filePath);

            // Setup license
            License lic = new License();
            lic.setLicense(licenseStream);
        } catch (Exception exp) {
            System.out.println("Exception: " + exp.getMessage());
            exp.printStackTrace();
        }
    }

    /**
     * This method writes input stream to output image file
     *
     * @param fileName      name of the output image file
     * @param imageFormat   format of output image file
     */
    public static void saveAsImage(String fileName, String imageFormat, InputStream inputStream) {
        try {
            // Write input stream to output file
            ImageIO.write(ImageIO.read(inputStream), imageFormat, new File(outputImagePath + getFileNameWithoutExtension(fileName) + "." + imageFormat));
        } catch (Exception exp) {
            System.out.println("Exception: " + exp.getMessage());
            exp.printStackTrace();
        }
    }

    /**
     * This method writes string to output html file
     *
     * @param outputFileName    name of the output html file
     * @param fileContent       content to be written in output html file
     */
    public static String saveAsHtml(String outputFileName, String fileContent) {
        String fileName = outputHtmlPath.toString() + getFileNameWithoutExtension(outputFileName) + ".html";
        try {
            // Initialize PrintWriter for output file
            PrintWriter out = new PrintWriter(fileName, StandardCharsets.UTF_8.name());

            // Write file content in
            out.println(fileContent);
            out.flush();
            out.close();
            return fileName;
        } catch (Exception exp) {
            System.out.println("Exception: " + exp.getMessage());
            exp.printStackTrace();
        }

        return null;
    }

    /**
     * This method writes input stream to output file
     *
     * @param fileName      name of the output file
     * @param inputStream   input stream to be written in output file
     */
    public static void saveAsFile(String fileName, InputStream inputStream) {
        try {
            // Create stream for output file
            OutputStream outputStream = new FileOutputStream(outputPath + fileName);
            int read = 0;
            byte[] bytes = new byte[1024];

            // Write bytes into output stream
            while ((read = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }
            outputStream.close();
            outputStream.flush();
        } catch (Exception exp) {
            System.out.println("Exception: " + exp.getMessage());
            exp.printStackTrace();
        }
    }

    /**
     * This method writes input stream to output file
     *
     * @param fileName      name of the output file
     * @param inputStream   input stream to be written in output file
     * @param fileExtension extension of output file
     */
    public static void saveAsFile(String fileName, InputStream inputStream, String fileExtension) {
        try {
            // Create stream for output file
            OutputStream outputStream = new FileOutputStream(outputPath + getFileNameWithoutExtension(fileName) + fileExtension);
            int read = 0;
            byte[] bytes = new byte[1024];

            // Write bytes into output stream
            while ((read = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }
            outputStream.close();
            outputStream.flush();
        } catch (Exception exp) {
            System.out.println("Exception: " + exp.getMessage());
            exp.printStackTrace();
        }
    }

    /**
     * This method gets extension of file
     *
     * @param fileName  name of the file
     * @return String file extension
     */
    public static String getFileExtension(String fileName) {
        try {
            // Get file name tokens
            String[] tokens = fileName.split("\\.(?=[^\\.]+$)");

            // Return file extension
            return tokens[tokens.length - 1];
        } catch (Exception exp) {
            System.out.println("Exception: " + exp.getMessage());
            exp.printStackTrace();
            return null;
        }
    }

    /**
     * This method gets extension of file
     *
     * @param fileName  name of the file
     */
    private static String getFileNameWithoutExtension(String fileName) {
        try {
            return fileName.replaceFirst("[.][^.]+$", "");
        } catch (Exception exp) {
            System.out.println("Exception: " + exp.getMessage());
            exp.printStackTrace();
            return null;
        }
    }

    /**
     * This method sets the storage path and Use Cache option
     *
     * @return ViewerConfig object
     */
    public static ViewerConfig getConfiguration() {
        try {
            // Setup GroupDocs.Viewer config
            ViewerConfig config = new ViewerConfig();
            // Set storage path
            config.setStoragePath(storagePath.toString());
            // Set cache to true for cache purpose
            config.setCachePath(tempPath.toString());
            return config;
        } catch (Exception exp) {
            System.out.println("Exception: " + exp.getMessage());
            exp.printStackTrace();
            return null;
        }
    }

    public static void GetDocumentRepresentationFromUri() throws Throwable {
        // Setup GroupDocs.Viewer config
        ViewerConfig config = new ViewerConfig();
        config.setStoragePath("C:\\storage");

        // Create image handler
        ViewerImageHandler imageHandler = new ViewerImageHandler(config);
        URI uri = new URI("http://groupdocs.com/images/banner/carousel2/signature.png");

        // Get pages by absolute path
        List<PageImage> pages = imageHandler.getPages(uri);
        System.out.println("Page count: " + pages.size());
    }

    public static void GetDocumentRepresentationFromInputStream() throws Throwable {
        // Setup GroupDocs.Viewer config
        ViewerConfig config = new ViewerConfig();
        config.setStoragePath("C:\\storage");

        // Create image handler
        ViewerImageHandler imageHandler = new ViewerImageHandler(config);

        FileInputStream fileStream = new FileInputStream("C:\\storage\\word.doc");

        // Get pages by absolute path
        List<PageImage> pages = imageHandler.getPages(fileStream, "word.doc");
        System.out.println("Page count: " + pages.size());
    }
}