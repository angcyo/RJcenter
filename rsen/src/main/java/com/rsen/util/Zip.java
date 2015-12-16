package com.rsen.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * Created by angcyo on 15-12-16 016 14:47 下午.
 */
public class Zip {

    public static final String ZIP_EXT = ".zip";

    /**
     * Gets compressor.
     *
     * @param file the file
     * @return the compressor
     * @throws FileNotFoundException the file not found exception
     */
    public static ZipCompressor getCompressor(File file) throws FileNotFoundException {
        return new ZipCompressor(file);
    }

    /**
     * 压缩文件/文件夹在当前目录
     *
     * @param file 需要压缩的文件/文件夹
     * @return 返回生成的压缩文件 file
     * @throws IOException the io exception
     */
    public static File zip(File file) throws IOException {
        StringBuilder zipPathBuilder = new StringBuilder();
        String zipName;
        if (file.isFile()) {
            zipName = FileUtil.getFileName(file.getName());
        } else if (file.isDirectory()) {
            zipName = file.getName();
        } else {
            throw new IllegalArgumentException("不支持的压缩");
        }
        zipPathBuilder.append(file.getParent())
                .append(File.separator)
                .append(zipName)
                .append(ZIP_EXT);

        File zipFile = new File(zipPathBuilder.toString());
        new ZipCompressor(zipFile).push(file);
        return zipFile;
    }

    /**
     * Zip file.
     *
     * @param path the path
     * @return the file
     * @throws IOException the io exception
     */
    public static File zip(String path) throws IOException {
        File file = new File(path);
        if (!file.exists()) {
            return null;
        }
        return zip(file);
    }

    /**
     * 解压压缩包.
     *
     * @param file 需要解压的文件, 默认解压在当前文件夹下相同文件名的文件夹中
     * @throws IOException the io exception
     */
    public static void unzip(File file) throws IOException {
        new ZipDeCompressor().unzip(file);
    }

    /**
     * 解压压缩包.
     *
     * @param file  需要解压的文件
     * @param toDir 解压到目标路径
     * @throws IOException the io exception
     */
    public static void unip(File file, File toDir) throws IOException {
        new ZipDeCompressor().unzip(file, toDir);
    }

    /**
     * The type Zip de compressor.
     */
    public static class ZipDeCompressor {
        /**
         * Unzip.
         *
         * @param file the file
         * @throws IOException the io exception
         */
        public void unzip(File file) throws IOException {

            String fileName = file.getName();
            int exindex = fileName.lastIndexOf(".");
            String dirName = fileName.substring(0, exindex);

            File toDir = new File(file.getParent(), dirName + "/");

            unzip(file, toDir);
        }

        /**
         * Unzip.
         *
         * @param file  the file
         * @param toDir the to dir
         * @throws IOException the io exception
         */
        public void unzip(File file, File toDir) throws IOException {

            toDir.mkdirs();
            if (!toDir.exists()) {
                throw new IllegalStateException();
            }

            ZipFile zipFile = new ZipFile(file);

            Enumeration<? extends ZipEntry> entries = zipFile.entries();

            int len;
            byte[] read = new byte[1024];

            while (entries.hasMoreElements()) {
                ZipEntry ze = entries.nextElement();

                File outFile = new File(toDir, ze.getName());
                if (ze.isDirectory()) {
                    outFile.mkdirs();
                } else {
                    BufferedInputStream bis = null;
                    BufferedOutputStream bos = null;
                    try {
                        InputStream is = zipFile.getInputStream(ze);
                        bis = new BufferedInputStream(is);

                        bos = new BufferedOutputStream(new FileOutputStream(
                                outFile));

                        while ((len = bis.read(read)) != -1) {
                            bos.write(read, 0, len);
                        }
                    } catch (FileNotFoundException e) {
                        throw e;
                    } catch (IOException e) {
                        throw e;
                    } finally {
                        try {
                            if (bis != null)
                                bis.close();
                        } catch (IOException e) {
                        }
                        try {
                            if (bos != null)
                                bos.close();
                        } catch (IOException e) {
                        }
                    }
                }
            }
        }
    }

    /**
     * The type Zip compressor.
     */
    public static class ZipCompressor {

        private File mZipFilePath;
        private File mProcessFile;
        private ZipOutputStream mZout;

        /**
         * Instantiates a new Zip compressor.
         *
         * @param zipFilePath the zip file path
         * @throws FileNotFoundException the file not found exception
         */
        ZipCompressor(File zipFilePath) throws FileNotFoundException {
            if (zipFilePath == null) {
                throw new IllegalArgumentException();
            }
            mZipFilePath = zipFilePath;
        }

        /**
         * Push.
         *
         * @param file the file
         * @throws IOException the io exception
         */
        public void push(File file) throws IOException {
            if (file == null) {
                throw new IllegalArgumentException();
            } else if (!file.exists()) {
                throw new FileNotFoundException("can't find "
                        + file.getAbsolutePath());
            }

            mProcessFile = file;

            if (mZout == null) {
                mZipFilePath.getParentFile().mkdirs();
                FileOutputStream fout = new FileOutputStream(mZipFilePath);
                mZout = new ZipOutputStream(fout);
            }

            if (file.isDirectory()) {
                pushDir(file);
            } else {
                pushFile(file);
            }

            finish();
        }

        /**
         * Finish.
         *
         * @throws IOException the io exception
         */
        public void finish() throws IOException {
            if (mZout == null) {
                throw new IllegalStateException("You need call push method.");
            }

            mZout.flush();
            mZout.close();
            mZout = null;
        }

        private void pushDir(File dir) throws IOException {

            ZipEntry entry = new ZipEntry(getZipEntryName(dir.getPath()) + "/");
            entry.setSize(0);
            mZout.putNextEntry(entry);

            List<File> rootFiles = Arrays.asList(dir.listFiles());

            for (File file : rootFiles) {
                if (file.isDirectory()) {
                    pushDir(file);
                } else {
                    pushFile(file);
                }
            }
        }

        private void pushFile(File file) throws FileNotFoundException,
                IOException {

            byte[] buf = new byte[8196];
            BufferedInputStream in = new BufferedInputStream(
                    new FileInputStream(file));

            ZipEntry entry = new ZipEntry(getZipEntryName(file.getPath()));
            mZout.putNextEntry(entry);

            int size;
            while ((size = in.read(buf, 0, buf.length)) != -1) {
                mZout.write(buf, 0, size);
            }

            mZout.closeEntry();
            in.close();
        }

        private String getZipEntryName(String filePath) {
            String parantPath = mProcessFile.getParent();
            parantPath = removeLastSeparator(parantPath);
            return filePath.substring(parantPath.length() + 1);
        }

        private String removeLastSeparator(String path) {
            if (!path.endsWith(File.pathSeparator)) {
                return path;
            }
            return path.substring(0, path.length() - 1);
        }
    }
}
