package com.ddx.solr.solrdemo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
public class SolrController {

    @Autowired
    private SolrService solrService;

    @PostMapping("/pushCsvData")
    public ResponseEntity<String> pushCsvData(@RequestParam("file") MultipartFile file,
                                              @RequestParam("collectionName") String collectionName) {
        try {
            solrService.indexCsvData(collectionName, file);
            return ResponseEntity.ok("CSV data has been pushed to Solr successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("Failed to push CSV data to Solr: " + e.getMessage());
        }
    }
}
