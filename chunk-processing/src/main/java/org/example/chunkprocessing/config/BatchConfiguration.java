package org.example.chunkprocessing.config;

import org.example.chunkprocessing.domain.*;
import org.example.chunkprocessing.processor.FilterDataItemProcessor;
import org.example.chunkprocessing.processor.TransformProductItemProcessor;
import org.example.chunkprocessing.reader.ProductNameItemReader;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.support.CompositeItemProcessor;
import org.springframework.batch.item.validator.BeanValidatingItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {
    @Autowired
    public JobBuilderFactory jobBuilderFactory;
    @Autowired
    public StepBuilderFactory stepBuilderFactory;
    @Autowired
    public DataSource dataSource;
    @Bean
    public ItemReader<String> itemReader() {
        List<String> productList = new ArrayList<>();
        productList.add("Product 1");
        productList.add("Product 2");
        productList.add("Product 3");
        productList.add("Product 4");
        productList.add("Product 5");
        productList.add("Product 6");
        productList.add("Product 7");
        productList.add("Product 8");
        return new ProductNameItemReader(productList);
    }

    // reader for reading data from flat file (csv)
    @Bean
    public ItemReader<Product> flatFileItemReader() {
        FlatFileItemReader<Product> itemReader = new FlatFileItemReader<>();
        itemReader.setLinesToSkip(1);
        itemReader.setResource(new ClassPathResource("/data/Product_Details.csv"));

        DefaultLineMapper<Product> lineMapper = new DefaultLineMapper<>();

        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setNames("product_id","product_name","product_category","product_price");

        lineMapper.setLineTokenizer(lineTokenizer);
        lineMapper.setFieldSetMapper(new ProductFieldSetMapper());

        itemReader.setLineMapper(lineMapper);

        return itemReader;
    }

    // reader for reading data from relational database
    @Bean
    public ItemReader<Product> jdbcCursorItemReader() {
        JdbcCursorItemReader<Product> itemReader = new JdbcCursorItemReader<>();
        itemReader.setDataSource(dataSource);
        itemReader.setSql("select * from product_details order by product_id");
        itemReader.setRowMapper(new ProductRowMapper());
        return itemReader;
    }

    // reader for reading data from relational database using paging
    @Bean
    public ItemReader<Product> jdbcPagingItemReader() throws Exception {
        JdbcPagingItemReader<Product> itemReader = new JdbcPagingItemReader<>();
        itemReader.setDataSource(dataSource);

        SqlPagingQueryProviderFactoryBean factory = new SqlPagingQueryProviderFactoryBean();
        factory.setDataSource(dataSource);
        factory.setSelectClause("select product_id, product_name, product_category, product_price");
        factory.setFromClause("from product_details");
        factory.setSortKey("product_id");

        itemReader.setQueryProvider(factory.getObject());
        itemReader.setRowMapper(new ProductRowMapper());
        itemReader.setPageSize(3);  // number of records to read at a time

        return itemReader;
    }

    // writer for writing data to flat file (csv)
    @Bean
    public ItemWriter<Product> flatFileItemWriter() {
        FlatFileItemWriter<Product> itemWriter = new FlatFileItemWriter<>();
        itemWriter.setResource(new FileSystemResource("src/main/resources/data/Product_Details_Output.csv"));

        DelimitedLineAggregator<Product> lineAggregator = new DelimitedLineAggregator<>();
        lineAggregator.setDelimiter(",");

        BeanWrapperFieldExtractor<Product> fieldExtractor = new BeanWrapperFieldExtractor<>();
        fieldExtractor.setNames(new String[]{"productId","productName","productCategory","productPrice"});

        lineAggregator.setFieldExtractor(fieldExtractor);

        itemWriter.setLineAggregator(lineAggregator);
        return itemWriter;
    }

    // writer for writing Product data to relational database
//    @Bean
//    public JdbcBatchItemWriter<Product> jdbcBatchItemWriter() {
//        JdbcBatchItemWriter<Product> itemWriter = new JdbcBatchItemWriter<>();
//        itemWriter.setDataSource(dataSource);
//        itemWriter.setSql("insert into product_details_output values(:productId,:productName,:productCategory,:productPrice)");
//        itemWriter.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
//        return itemWriter;
//    }

    // writer for writing OSProduct data to relational database
    @Bean
    public JdbcBatchItemWriter<OSProduct> jdbcBatchItemWriter() {
        JdbcBatchItemWriter<OSProduct> itemWriter = new JdbcBatchItemWriter<>();
        itemWriter.setDataSource(dataSource);
        itemWriter.setSql("insert into os_product_details values(:productId,:productName,:productCategory,:productPrice, :taxPercent, :sku, :shippingRate)");
        itemWriter.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
        return itemWriter;
    }

    @Bean
    public ItemProcessor<Product, OSProduct> transformProductItemProcessor() {
        return new TransformProductItemProcessor();
    }

    @Bean
    public ItemProcessor<Product, Product> filterProductItemProcessor() {
        return new FilterDataItemProcessor();
    }

//    @Bean
//    public ValidatingItemProcessor<Product> validateProductItemProcessor() {
//        ValidatingItemProcessor<Product> itemProcessor = new ValidatingItemProcessor<>(new ProductValidator());
//        itemProcessor.setFilter(true);
//        return itemProcessor;
////        return new ValidatingItemProcessor<>(new ProductValidator());
//    }

    @Bean
    public BeanValidatingItemProcessor<Product> validateProductItemProcessor() {
        BeanValidatingItemProcessor<Product> beanValidatingItemProcessor = new BeanValidatingItemProcessor<>();
        beanValidatingItemProcessor.setFilter(true);
        return beanValidatingItemProcessor;
//        return new BeanValidatingItemProcessor<>();
    }

    @Bean
    public CompositeItemProcessor<Product, OSProduct> itemProcessor() {
        CompositeItemProcessor<Product, OSProduct> itemProcessor = new CompositeItemProcessor<>();
        List itemProcessors = new ArrayList();
        itemProcessors.add(validateProductItemProcessor());
        itemProcessors.add(filterProductItemProcessor());
        itemProcessors.add(transformProductItemProcessor());
        itemProcessor.setDelegates(itemProcessors);
        return itemProcessor;
    }

    @Bean
    public Step step1() throws Exception {
        return this.stepBuilderFactory.get("chunkBasedStep1")
                .<Product,OSProduct>chunk(3) // <String, String> means input and output type of the chunk
                .reader(jdbcPagingItemReader())
                .processor(itemProcessor())
                .writer(jdbcBatchItemWriter())
                .build();
    }

    @Bean
    public Job firstJob() throws Exception {
        return this.jobBuilderFactory.get("job1")
                .start(step1())
                .build();
    }
}
