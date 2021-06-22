package com.google.cloud.teleport.v2.elasticsearch.transforms;

import com.google.cloud.teleport.v2.elasticsearch.options.ElasticsearchWriteOptions;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.testing.TestPipeline;
import org.apache.beam.sdk.transforms.Create;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/** Test cases for the {@link WriteToElasticsearch} class. */
public class WriteToElasticsearchTest {
    
    @Rule
    public final transient TestPipeline pipeline = TestPipeline.create();

    @Rule public ExpectedException exceptionRule = ExpectedException.none();

    /** Tests {@link WriteToElasticsearch} throws an exception if a null node address is provided. */
    @Test
    public void testNullNodeAddresses() {

        exceptionRule.expect(IllegalArgumentException.class);

        ElasticsearchWriteOptions options =
                PipelineOptionsFactory.create()
                        .as(ElasticsearchWriteOptions.class);

        options.setTargetNodeAddresses(null);
        options.setWriteIndex("testIndex");
        options.setWriteDocumentType("testType");

        pipeline
                .apply("CreateInput", Create.of("test"))
                .apply(WriteToElasticsearch.newBuilder().setOptions(options).build());

        pipeline.run();
    }

    /** Tests {@link WriteToElasticsearch} throws an exception if a null index is provided. */
    @Test
    public void testNullType() {

        exceptionRule.expect(IllegalArgumentException.class);

        ElasticsearchWriteOptions options =
                PipelineOptionsFactory.create()
                        .as(ElasticsearchWriteOptions.class);

        options.setTargetNodeAddresses("http://my-node1");
        options.setWriteIndex("testIndex");
        options.setWriteDocumentType(null);

        pipeline
                .apply("CreateInput", Create.of("test"))
                .apply(WriteToElasticsearch.newBuilder().setOptions(options).build());

        pipeline.run();
    }

    /** Tests {@link WriteToElasticsearch} throws an exception if a null index is provided. */
    @Test
    public void testNullIndex() {

        exceptionRule.expect(IllegalArgumentException.class);

        ElasticsearchWriteOptions options =
                PipelineOptionsFactory.create()
                        .as(ElasticsearchWriteOptions.class);

        options.setTargetNodeAddresses("http://my-node1");
        options.setWriteIndex(null);
        options.setWriteDocumentType("testType");

        pipeline
                .apply("CreateInput", Create.of("test"))
                .apply(WriteToElasticsearch.newBuilder().setOptions(options).build());

        pipeline.run();
    }

    /** Tests that {@link WriteToElasticsearch} throws an exception if an invalid node address is provided. */
    @Test
    public void testInvalidNodeAddresses() {

        exceptionRule.expect(IllegalArgumentException.class);

        ElasticsearchWriteOptions options =
                PipelineOptionsFactory.create()
                        .as(ElasticsearchWriteOptions.class);

        options.setTargetNodeAddresses(",");
        options.setWriteIndex("testIndex");
        options.setWriteDocumentType("testType");

        pipeline
                .apply("CreateInput", Create.of("test"))
                .apply(
                        "TestWriteToElasticsearch",
                        WriteToElasticsearch.newBuilder().setOptions(options).build());

        pipeline.run();
    }

    /** Tests that {@link WriteToElasticsearch} throws an exception if {@link org.apache.beam.sdk.io.elasticsearch.ElasticsearchIO.RetryConfiguration} are invalid. */
    @Test
    public void testElasticsearchWriteOptionsRetryConfigMaxAttempts() {

        exceptionRule.expect(IllegalArgumentException.class);

        ElasticsearchWriteOptions options =
                PipelineOptionsFactory.create()
                        .as(ElasticsearchWriteOptions.class);

        options.setTargetNodeAddresses("http://my-node1");
        options.setWriteIndex("testIndex");
        options.setWriteDocumentType("testDoc");
        options.setMaxRetryDuration(500L);
        options.setMaxRetryAttempts(null);

        pipeline
                .apply("CreateInput", Create.of("test"))
                .apply(
                        "TestWriteToElasticsearchBadMaxAttempts",
                        WriteToElasticsearch.newBuilder().setOptions(options).build());

        pipeline.run();
    }

    /** Tests that {@link WriteToElasticsearch} throws an exception if {@link org.apache.beam.sdk.io.elasticsearch.ElasticsearchIO.RetryConfiguration} are invalid. */
    @Test
    public void testElasticsearchWriteOptionsRetryConfigMaxDuration() {

        exceptionRule.expect(IllegalArgumentException.class);

        ElasticsearchWriteOptions options =
                PipelineOptionsFactory.create()
                        .as(ElasticsearchWriteOptions.class);

        options.setTargetNodeAddresses("http://my-node1");
        options.setWriteIndex("testIndex");
        options.setWriteDocumentType("testDoc");
        options.setMaxRetryDuration(null);
        options.setMaxRetryAttempts(3);

        pipeline
                .apply("CreateInput", Create.of("test"))
                .apply(
                        "TestWriteToElasticsearchBadMaxDuration",
                        WriteToElasticsearch.newBuilder().setOptions(options).build());

        pipeline.run();
    }
}
