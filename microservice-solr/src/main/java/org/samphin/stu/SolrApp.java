package org.samphin.stu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigurationImportSelector;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@SpringBootConfiguration
@Import({AutoConfigurationImportSelector.class})
@ComponentScan({"org.samphin.stu.*",""})
public class SolrApp {
    public static void main(String[] args) {
        SpringApplication.run(SolrApp.class, args);
    }
}
