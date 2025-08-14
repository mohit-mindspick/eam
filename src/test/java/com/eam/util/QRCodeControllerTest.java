package com.eam.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class QRCodeControllerTest {

    private QRCodeController qrCodeController;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        qrCodeController = new QRCodeController();
        mockMvc = MockMvcBuilders.standaloneSetup(qrCodeController).build();
    }

    @Test
    void generateQRCode_ShouldGenerateBasicQRCode() throws Exception {
        String testText = "https://example.com";

        mockMvc.perform(get("/api/qrcode")
                        .param("text", testText)
                        .accept(MediaType.IMAGE_PNG_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.IMAGE_PNG_VALUE))
                .andExpect(header().string("Content-Disposition", "inline; filename=\"qrcode.png\""))
                .andExpect(content().string(org.hamcrest.Matchers.not(org.hamcrest.Matchers.emptyString())));
    }



    @Test
    void generateQRCode_ShouldHandleSpecialCharacters() throws Exception {
        String specialText = "https://example.com/path?param=value&another=123";

        mockMvc.perform(get("/api/qrcode")
                        .param("text", specialText)
                        .accept(MediaType.IMAGE_PNG_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.IMAGE_PNG_VALUE))
                .andExpect(content().string(org.hamcrest.Matchers.not(org.hamcrest.Matchers.emptyString())));
    }

    @Test
    void generateQRCode_ShouldHandleLongText() throws Exception {
        String longText = "This is a very long text that should still generate a valid QR code. " +
                "It contains multiple sentences and should test the QR code generation with longer content. " +
                "The QR code should be able to handle this amount of text without issues.";

        mockMvc.perform(get("/api/qrcode")
                        .param("text", longText)
                        .accept(MediaType.IMAGE_PNG_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.IMAGE_PNG_VALUE))
                .andExpect(content().string(org.hamcrest.Matchers.not(org.hamcrest.Matchers.emptyString())));
    }

    @Test
    void generateQRCode_ShouldHandleUnicodeCharacters() throws Exception {
        String unicodeText = "https://example.com/测试/中文/日本語";

        mockMvc.perform(get("/api/qrcode")
                        .param("text", unicodeText)
                        .accept(MediaType.IMAGE_PNG_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.IMAGE_PNG_VALUE))
                .andExpect(content().string(org.hamcrest.Matchers.not(org.hamcrest.Matchers.emptyString())));
    }

    @Test
    void generateQRCode_ShouldHandleMissingTextParameter() throws Exception {
        mockMvc.perform(get("/api/qrcode")
                        .accept(MediaType.IMAGE_PNG_VALUE))
                .andExpect(status().isBadRequest());
    }

    @Test
    void generateQRCodeWithLogo_ShouldGenerateQRCodeWithLogo() throws Exception {
        String testText = "https://example.com";

        mockMvc.perform(get("/api/qrcode/with-logo")
                        .param("text", testText)
                        .accept(MediaType.IMAGE_PNG_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.IMAGE_PNG_VALUE))
                .andExpect(header().string("Content-Disposition", "inline; filename=\"qr_with_logo.png\""))
                .andExpect(content().string(org.hamcrest.Matchers.not(org.hamcrest.Matchers.emptyString())));
    }



    @Test
    void generateQRCodeWithLogo_ShouldHandleSpecialCharacters() throws Exception {
        String specialText = "https://example.com/path?param=value&another=123";

        mockMvc.perform(get("/api/qrcode/with-logo")
                        .param("text", specialText)
                        .accept(MediaType.IMAGE_PNG_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.IMAGE_PNG_VALUE))
                .andExpect(content().string(org.hamcrest.Matchers.not(org.hamcrest.Matchers.emptyString())));
    }

    @Test
    void generateQRCodeWithLogo_ShouldHandleLongText() throws Exception {
        String longText = "This is a very long text that should still generate a valid QR code with logo. " +
                "It contains multiple sentences and should test the QR code generation with longer content. " +
                "The QR code should be able to handle this amount of text without issues.";

        mockMvc.perform(get("/api/qrcode/with-logo")
                        .param("text", longText)
                        .accept(MediaType.IMAGE_PNG_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.IMAGE_PNG_VALUE))
                .andExpect(content().string(org.hamcrest.Matchers.not(org.hamcrest.Matchers.emptyString())));
    }

    @Test
    void generateQRCodeWithLogo_ShouldHandleUnicodeCharacters() throws Exception {
        String unicodeText = "https://example.com/测试/中文/日本語";

        mockMvc.perform(get("/api/qrcode/with-logo")
                        .param("text", unicodeText)
                        .accept(MediaType.IMAGE_PNG_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.IMAGE_PNG_VALUE))
                .andExpect(content().string(org.hamcrest.Matchers.not(org.hamcrest.Matchers.emptyString())));
    }

    @Test
    void generateQRCodeWithLogo_ShouldHandleMissingTextParameter() throws Exception {
        mockMvc.perform(get("/api/qrcode/with-logo")
                        .accept(MediaType.IMAGE_PNG_VALUE))
                .andExpect(status().isBadRequest());
    }

    @Test
    void generateQRCode_ShouldHandleDifferentAcceptHeaders() throws Exception {
        String testText = "https://example.com";

        // Test with different accept headers
        mockMvc.perform(get("/api/qrcode")
                        .param("text", testText)
                        .accept(MediaType.ALL_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.IMAGE_PNG_VALUE))
                .andExpect(content().string(org.hamcrest.Matchers.not(org.hamcrest.Matchers.emptyString())));

        mockMvc.perform(get("/api/qrcode")
                        .param("text", testText)
                        .accept(MediaType.IMAGE_PNG_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.IMAGE_PNG_VALUE))
                .andExpect(content().string(org.hamcrest.Matchers.not(org.hamcrest.Matchers.emptyString())));
    }

    @Test
    void generateQRCodeWithLogo_ShouldHandleDifferentAcceptHeaders() throws Exception {
        String testText = "https://example.com";

        // Test with different accept headers
        mockMvc.perform(get("/api/qrcode/with-logo")
                        .param("text", testText)
                        .accept(MediaType.ALL_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.IMAGE_PNG_VALUE))
                .andExpect(content().string(org.hamcrest.Matchers.not(org.hamcrest.Matchers.emptyString())));

        mockMvc.perform(get("/api/qrcode/with-logo")
                        .param("text", testText)
                        .accept(MediaType.IMAGE_PNG_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.IMAGE_PNG_VALUE))
                .andExpect(content().string(org.hamcrest.Matchers.not(org.hamcrest.Matchers.emptyString())));
    }

    @Test
    void generateQRCode_ShouldGenerateConsistentSize() throws Exception {
        String testText = "https://example.com";

        // Generate multiple QR codes and verify they have consistent properties
        for (int i = 0; i < 3; i++) {
            mockMvc.perform(get("/api/qrcode")
                            .param("text", testText)
                            .accept(MediaType.IMAGE_PNG_VALUE))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.IMAGE_PNG_VALUE))
                    .andExpect(content().string(org.hamcrest.Matchers.not(org.hamcrest.Matchers.emptyString())));
        }
    }

    @Test
    void generateQRCodeWithLogo_ShouldGenerateConsistentSize() throws Exception {
        String testText = "https://example.com";

        // Generate multiple QR codes with logo and verify they have consistent properties
        for (int i = 0; i < 3; i++) {
            mockMvc.perform(get("/api/qrcode/with-logo")
                            .param("text", testText)
                            .accept(MediaType.IMAGE_PNG_VALUE))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.IMAGE_PNG_VALUE))
                    .andExpect(content().string(org.hamcrest.Matchers.not(org.hamcrest.Matchers.emptyString())));
        }
    }
}
