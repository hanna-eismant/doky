package org.hkurh.doky.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.hkurh.doky.controllers.data.DocumentRequest;
import org.hkurh.doky.dto.DocumentDto;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "Documents")
@SecurityRequirement(name = "Bearer Token")
public interface DocumentApi {

    @Operation(summary = "Upload file to document entry")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "File is uploaded and attached to document"),
            @ApiResponse(responseCode = "404", description = "Document with provided id does not exist")
    })
    ResponseEntity<?> uploadFile(@RequestBody MultipartFile file, @PathVariable String id);

    @Operation(summary = "Download file attached to document entry")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "File is sent to client and can be downloaded",
                    content = @Content(mediaType = MediaType.APPLICATION_OCTET_STREAM_VALUE),
                    headers = @Header(name = "attachment; filename=...")),
            @ApiResponse(responseCode = "204", description = "No document with requested id, or no attached file for document")
    })
    ResponseEntity<?> downloadFile(@PathVariable String id);

    @Operation(summary = "Create document with metadata")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Document is created")
    })
    ResponseEntity<?> create(@RequestBody DocumentRequest document);

    @Operation(summary = "Get all documents that was created by current customer")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List if documents is retrieved successfully",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = DocumentDto.class)))),
            @ApiResponse(responseCode = "204", description = "No documents found for current user")
    })
    ResponseEntity<?> getAll();

    @Operation(summary = "Get metadata for document")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Document information is retrieved successfully",
                    content = @Content(schema = @Schema(implementation = DocumentDto.class))),
            @ApiResponse(responseCode = "204", description = "No document with provided id")
    })
    ResponseEntity<?> get(@PathVariable String id);
}
