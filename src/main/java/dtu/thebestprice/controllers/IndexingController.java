package dtu.thebestprice.controllers;

import dtu.thebestprice.services.IndexingService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/indexing")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class IndexingController {
    @Autowired
    IndexingService indexingService;

    @PostMapping
    @ApiOperation(value = "Đánh index lại")
    public void startIndex() {
        indexingService.initiateIndexing();
    }
}
