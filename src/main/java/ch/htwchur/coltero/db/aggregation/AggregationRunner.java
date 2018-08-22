package ch.htwchur.coltero.db.aggregation;

import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.scheduler.JobRunner;
import com.atlassian.scheduler.JobRunnerRequest;
import com.atlassian.scheduler.JobRunnerResponse;

import ch.htwchur.coltero.dashboard.logic.UserInteractionLogic;
import ch.htwchur.coltero.dashboard.logic.extraction.HtmlContentExtractor;
import ch.htwchur.coltero.dashboard.mngtdashboard.queries.MngtCountQueries;

/**
 * Scheduled Task to aggregate dashboard data. Intervals can be changed in
 * confluence or plugin-descriptor.xml
 * 
 * @author sandro.hoerler@htwchur.ch
 *
 */
public class AggregationRunner implements JobRunner {
	private static final Logger log = LoggerFactory.getLogger(MngtCountQueries.class);
	private AggregateDashboardData aggregateDashboardData;
	private HtmlContentExtractor contentExtractor;
	private UserInteractionLogic userInteractionLogic;

	@Autowired
	public AggregationRunner(@ComponentImport AggregateDashboardData aggregateDashboardData,
			@ComponentImport HtmlContentExtractor contentExtractor,
			@ComponentImport UserInteractionLogic userInteractionLogic) {
		this.aggregateDashboardData = aggregateDashboardData;
		this.contentExtractor = contentExtractor;
		this.userInteractionLogic = userInteractionLogic;
	}

	@Override
	public JobRunnerResponse runJob(JobRunnerRequest request) {
		try {
			if (aggregateDashboardData.preAggregateData()) {
				contentExtractor.aggregateSpaceWordCount();
				contentExtractor.aggregateSpaceCommentWordCount();
				userInteractionLogic.aggregateUserInteractions(userInteractionLogic.calcUserInteractions());
				contentExtractor.aggregateUserWordCount();
			}
		} catch (SQLException e) {
			log.error("Could not execute Scheduled Job named AggregateDashboardData due to Database Error:"
					+ e.getCause() + " " + e.getMessage());
			return JobRunnerResponse.failed(e.getCause());
		}
		log.info("Aggregations successfully executed...");
		return JobRunnerResponse.success("Successfully executed Job named AggregateDashboardData");
	}

}
