package com.usic.qr_fest.model.Service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Base64;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.usic.qr_fest.model.Entity.Entrada;
import com.usic.qr_fest.model.Entity.Titulo;
import com.usic.qr_fest.model.IService.IEntradaService;
import com.usic.qr_fest.model.IService.ITituloService;

@Service
public class QRService {

    @Autowired
    private IEntradaService entradaService;

    @Autowired
    private ITituloService tituloService;

    private static final String AlGORITHM = "AES";
    private static final String SECRET_KEY = "1234567890123456";

    public void generarQrs(int cantidad, String titulo) throws Exception {

        Titulo titulo_encontrado = tituloService.findByTitulo(titulo);
        if (titulo_encontrado == null) {
            titulo_encontrado = new Titulo();
            titulo_encontrado.setTitulo(titulo);
            titulo_encontrado.setEstado("ACTIVO");
            tituloService.save(titulo_encontrado);
        }

        String outputDir = "qrs";
        File outputDirFile = new File(outputDir);
        if (outputDirFile.exists()) {
            for(File file : outputDirFile.listFiles()){
                file.delete();
            }
        }else{
            Files.createDirectories(Paths.get(outputDir));// Crea el directorio si no existe
        }

        for (int i = 1; i <= cantidad; i++) {
            String uniqueId = UUID.randomUUID().toString();
            String id = String.format("%04d", i); // Genera un identificador en formato 0001, 0002, etc.
            String dataToEncrypt = id + "_" + uniqueId;
            String encryptedId = encrypt(dataToEncrypt);

            // Genera el QR
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(encryptedId, BarcodeFormat.QR_CODE, 350, 350);
            String fileName = outputDir + "/QR_" + id + ".png";
            MatrixToImageWriter.writeToPath(bitMatrix, "PNG", Paths.get(fileName));

            Entrada entrada = new Entrada();
            entrada.setIdentificador(encryptedId);
            entrada.setEstado("NO APROBADO");
            entrada.setTitulo(titulo_encontrado);
            entradaService.save(entrada);
        }

        String zipFileName = "qrs_" + titulo.replace(" ", "_") + "_" + System.currentTimeMillis() + ".zip";
        exportToZip(outputDir, zipFileName);
    }

    private void exportToZip(String dir, String zipFileName) throws Exception {

        File staticFolder = new File("src/main/resources/static/");
        File[] oldZipFiles = staticFolder.listFiles((dir1, name) -> name.startsWith("qrs_") && name.endsWith(".zip"));
        if (oldZipFiles != null) {
            for (File oldZipFile : oldZipFiles) {
                oldZipFile.delete();
            }
        }

        try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipFileName))) {
            File folder = new File(dir);
            for (File file : folder.listFiles()) {
                if (!file.isFile()) continue;
                ZipEntry zipEntry = new ZipEntry(file.getName());
                zos.putNextEntry(zipEntry);
                Files.copy(file.toPath(), zos);
                zos.closeEntry();
            }
        }

        File zipFile = new File(zipFileName);
        File zipDestination = new File("src/main/resources/static/" + zipFileName);
        Files.move(zipFile.toPath(), zipDestination.toPath(), StandardCopyOption.REPLACE_EXISTING);
    }

    private String encrypt(String data) throws Exception {
        Cipher cipher = Cipher.getInstance(AlGORITHM);
        SecretKeySpec keySpec = new SecretKeySpec(SECRET_KEY.getBytes(), AlGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, keySpec);
        byte[] encrypted = cipher.doFinal(data.getBytes());
        return Base64.getEncoder().encodeToString(encrypted);
    }

    public String desencriptarIdentificador(String identificadorEncriptado) {
        try {
            Cipher cipher = Cipher.getInstance(AlGORITHM);
            SecretKeySpec keySpec = new SecretKeySpec(SECRET_KEY.getBytes(), AlGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, keySpec);
            byte[] decodedBytes = Base64.getDecoder().decode(identificadorEncriptado);
            byte[] decryptedBytes = cipher.doFinal(decodedBytes);
            return new String(decryptedBytes);
        } catch (Exception e) {
            throw new RuntimeException("Error desencriptando el identificador", e);
        }
    }
    

}
