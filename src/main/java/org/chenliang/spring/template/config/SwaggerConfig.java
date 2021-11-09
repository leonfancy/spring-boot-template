package org.chenliang.spring.template.config;

import com.fasterxml.classmate.TypeResolver;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.data.domain.Pageable;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.AlternateTypeRule;
import springfox.documentation.schema.AlternateTypeRuleConvention;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static java.util.Collections.singletonList;
import static springfox.documentation.schema.AlternateTypeRules.newRule;

@Configuration
public class SwaggerConfig {
  @Bean
  public Docket api() {
    return new Docket(DocumentationType.SWAGGER_2)
        .select()
        .paths(PathSelectors.any())
        .apis(RequestHandlerSelectors.basePackage("com.chenliang.spring.template"))
        .build()
        .directModelSubstitute(LocalDate.class, String.class)
        .enableUrlTemplating(true)
        .apiInfo(apiInfo());
  }

  private ApiInfo apiInfo() {
    return new ApiInfo(
        "Demo API",
        "Demo API",
        "v1",
        "",
        null,
        "chenliang.org", "", Collections.emptyList());
  }

  @Bean
  public AlternateTypeRuleConvention pageableConvention(final TypeResolver resolver) {
    return new AlternateTypeRuleConvention() {

      @Override
      public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
      }

      @Override
      public List<AlternateTypeRule> rules() {
        return singletonList(
            newRule(
                resolver.resolve(Pageable.class),
                resolver.resolve(SimplePage.class)
            )
        );
      }
    };
  }

  @Data
  public static class SimplePage {
    @ApiModelProperty(value = "Page number, starting from 0", example = "1")
    private Integer page;

    @ApiModelProperty(value = "Number of elements of a page", example = "20")
    private Integer size;

    @ApiModelProperty(value = "Sorting criteria in the format: <code>property(,asc|desc)</code>. Default sort order is ascending. " +
                              "Multiple sort criteria are supported.", example = "propertyname,desc")
    private String sort;
  }
}
