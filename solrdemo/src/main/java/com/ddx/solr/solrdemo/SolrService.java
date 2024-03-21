package com.ddx.solr.solrdemo;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.request.UpdateRequest;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.BufferedReader;
import java.io.InputStreamReader;

@Service
public class SolrService {
	
	 @Value("${solr.url}")
	 private String solrUrl;


    public void indexCsvData(String collectionName, MultipartFile file) throws Exception {
        SolrClient solrClient = new HttpSolrClient.Builder(solrUrl).build();
        try {
            UpdateRequest request = new UpdateRequest();
            request.setCommitWithin(10000); // Commit within 10 seconds (adjust as needed)

            BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()));
            String line;
            while ((line = br.readLine()) != null) {
                // Assuming CSV format: id,field1,field2,...
                String[] fields = line.split(",");
                if (fields.length < 1) continue; // Skip empty lines

                SolrInputDocument document = new SolrInputDocument();
                document.addField("id", fields[0]); // Assuming the first column is the unique ID

                // Add other fields
                for (int i = 1; i < fields.length; i++) {
                    document.addField("field" + i, fields[i]);
                }

                request.add(document);
            }
            br.close();

            request.process(solrClient, collectionName);
        } finally {
            solrClient.close();
        }
    }
}
