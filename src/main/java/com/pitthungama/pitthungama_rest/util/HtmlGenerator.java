package com.pitthungama.pitthungama_rest.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class HtmlGenerator {
    private TemplateEngine templateEngine;
    private String assetUrl;

    @Autowired
    HtmlGenerator(TemplateEngine templateEngine, @Value("${app-config.thymeleaf-template-assets-path}") String assetUrl){
        this.assetUrl = assetUrl;
        this.templateEngine = templateEngine;
    }

    public String generate(Context context, String templateFileName) {
        context.setVariable("assetUrl", this.assetUrl);
        return this.templateEngine.process(templateFileName, context);
    }
}
