package base.io;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ZipTest {

    @Test
    public void test() throws IOException {
        String zipDir = "D:/tmp/testmysqlreader/testmysqlreader.zip";
        ZipInputStream zis = null;
        try {
            zis = new ZipInputStream(new FileInputStream(zipDir));
            dounzip(zis, "D:/tmp/123/");
        } finally {
            zis.close();
        }

    }

    public void dounzip(ZipInputStream zipInputStream, String localDir) {
        try {
            //这里filename是文件名，如xxx.zip
            ZipEntry fentry = null;
            while ((fentry = zipInputStream.getNextEntry()) != null) {
                System.out.println("-------------------" + fentry.getName());
                if (fentry.isDirectory()) {
                    File dir = new File(localDir + fentry.getName());
                    if (!dir.exists()) {
                        dir.mkdirs();
                    }
                } else {
                    //fname是文件名,fileoutputstream与该文件名关联
                    String fname = new String(localDir + fentry.getName());
                    FileOutputStream output = new FileOutputStream(fname);
                    try {
                        IOUtils.copy(zipInputStream, output);
                        output.close(); // don't swallow close Exception if copy completes normally
                    } finally {
                        IOUtils.closeQuietly(output);
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        System.out.println("finished!");
    }

}
