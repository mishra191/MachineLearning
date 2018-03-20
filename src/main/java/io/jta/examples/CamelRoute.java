package io.jta.examples;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.model.dataformat.BindyType;
import org.apache.camel.spring.SpringRouteBuilder;
import org.springframework.stereotype.Component;

@Component
/**
 * Rewriting the application of distributed transaction Now you can start
 * doing---- How is this working is very important-----
 * 
 * @author Anurag
 *
 */
public class CamelRoute extends SpringRouteBuilder {

	private boolean fail = false;
	private FailProcessor failProcessor = new FailProcessor();

	@Override
	/**
	 * awesome configuration----------------- where is the error queue??? where
	 * is SFTP?
	 */
	public void configure() throws Exception {
		
        transactionErrorHandler()
                .maximumRedeliveries(0);

        onException(Exception.class)
                .maximumRedeliveries(0)
                .handled(false);
        
        
        from("file:" + folderPath)
        .log("Start processing ....")
        .multicast()
        .parallelProcessing()
        .to("direct:database-save", "direct:move-to-out")
        .end();

        from("direct:database-save")
                        .log("Start saving to database ....")
                        .split()
                        .tokenize("\n")
                        .streaming()
                        .filter(simple("${body.length} > 30"))
                        .unmarshal()
                        .bindy(BindyType.Csv, CSVRecord.class)
                        .bean(csvToModelConverter, "convertToMysqlModel")
                        .aggregate(constant(true), batchAggregationStrategy())
                        .completionPredicate(batchSizePredicate())
                        .completionTimeout(BATCH_TIME_OUT)
                        .bean(persistService)
                        .log("End saving to database ....")
                        .end();

        from("direct:move-to-out")
                        .setHeader(Exchange.FILE_NAME,
                                        simple("${file:name.noext}-${date:now:yyyyMMddHHmmssSSS}.${file:ext}"))
                        .to("file:" + folderPathout)
                        .end();
        

//        from("file:C:/Users/Anurag/Downloads/atomikos-jta-camel-jpa-jms-example-master/target/outbox/dvd")
//        .log("Start processing ....")
//        .multicast()
//        .parallelProcessing()
//        .to("direct:putOnQueue")
//        .end();

     //   from("file:C:/Users/Anurag/Downloads/atomikos-jta-camel-jpa-jms-example-master/target/outbox/dvd")
//        from(("direct:putOnQueue")
//                .transacted()
//                .split(body().tokenize(","))
//                .to("jms:queue:person2Db")
////                .to("jms:queue:person2Queue")
//                .end();
//        
//
//
//        from("jms:queue:person2Db")
//                .transacted()
//                .convertBodyTo(Person.class)
//                .to("jpa:io.strandberg.jta.examples.Person");
//
//
////		transactionErrorHandler().maximumRedeliveries(0);
////
////		onException(Exception.class).maximumRedeliveries(0).handled(false);
////
////		//from("file:target/outbox/dvd").
////		from("direct:putOnQueue").
////		//bean(new TransformationBean(), "makeUpperCase").
////		transacted().split(body().tokenize(","))
////		
////				.to("jms:queue:person2Db")				
////				.to("jms:queue:person2Queue")
//////				.choice().when(body().contains("DVD"))
//////				.to("file:target/outbox/dvd")
//////				.when(body().contains("CD"))
//////				.to("activemq:CD_Orders")
//////				.otherwise()
//////				.to("mock:others")
////				.process(failProcessor);
////
////		from("jms:queue:person2Db").
//		transacted().
//		convertBodyTo(Person.class)
//				
//		.to("jpa:io.strandberg.jta.examples.Person");
	}

	private class FailProcessor implements Processor {
		public void process(Exchange exchange) throws Exception {
			if (fail)
				throw new IllegalStateException("BOOM---BOOM");
		}
	}

	public void setFail(boolean fail) {
		this.fail = fail;
	}

}
