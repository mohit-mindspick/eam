package com.eam.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

@RestController
@RequestMapping("/api/qrcode")
@CrossOrigin(origins = "*")
@Tag(name = "QR Code Generation", description = "APIs for generating QR codes with or without custom logos")
public class QRCodeController {

    @GetMapping(produces = MediaType.IMAGE_PNG_VALUE)
    @Operation(
        summary = "Generate Basic QR Code",
        description = "Generates a basic QR code image from the provided text"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "QR code generated successfully",
            content = @Content(
                mediaType = "image/png",
                schema = @Schema(type = "string", format = "binary")
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid text input"
        )
    })
    public ResponseEntity<byte[]> generateQRCode(
        @Parameter(description = "Text to encode in QR code", required = true, example = "https://example.com")
        @RequestParam String text) throws WriterException, IOException {
        int width = 300;
        int height = 300;

        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);

        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
        byte[] pngData = pngOutputStream.toByteArray();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"qrcode.png\"")
                .contentType(MediaType.IMAGE_PNG)
                .body(pngData);
    }

    @GetMapping(value = "/with-logo", produces = MediaType.IMAGE_PNG_VALUE)
    @Operation(
        summary = "Generate QR Code with Logo",
        description = "Generates a QR code with an embedded logo overlay"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "QR code with logo generated successfully",
            content = @Content(
                mediaType = "image/png",
                schema = @Schema(type = "string", format = "binary")
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid text input or logo not found"
        )
    })
    public ResponseEntity<byte[]> generateQRCodeWithLogo(
        @Parameter(description = "Text to encode in QR code", required = true, example = "https://example.com")
        @RequestParam String text) throws IOException, WriterException {
        int size = 300;

        // Step 1: Generate QR Code
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, size, size);
        BufferedImage qrImage = MatrixToImageWriter.toBufferedImage(bitMatrix);

        // Step 2: Load Logo Image
        InputStream logoStream = getClass().getResourceAsStream("/static/logo.png"); // Place your logo in src/main/resources/static
        BufferedImage logo = ImageIO.read(logoStream);

        // Step 3: Overlay Logo
        int logoWidth = qrImage.getWidth() / 5;
        int logoHeight = qrImage.getHeight() / 5;
        int centerX = (qrImage.getWidth() - logoWidth) / 2;
        int centerY = (qrImage.getHeight() - logoHeight) / 2;

        Graphics2D g = qrImage.createGraphics();
        g.setComposite(AlphaComposite.DstOver);
        g.drawImage(logo, centerX, centerY, logoWidth, logoHeight, null);
        g.dispose();

        // Step 4: Convert to byte[]
        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        ImageIO.write(qrImage, "PNG", pngOutputStream);
        byte[] pngData = pngOutputStream.toByteArray();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"qr_with_logo.png\"")
                .contentType(MediaType.IMAGE_PNG)
                .body(pngData);
    }
}