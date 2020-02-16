package pl.edu.agh.gastronomiastosowana.model.reports;

import pl.edu.agh.gastronomiastosowana.dao.RatingDao;
import pl.edu.agh.gastronomiastosowana.model.Participant;
import pl.edu.agh.gastronomiastosowana.model.ProjectGroup;
import pl.edu.agh.gastronomiastosowana.model.Rating;
import pl.edu.agh.gastronomiastosowana.model.ratings.RatingAggregator;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;

public class SimpleTextProjectGroupRatingsSummaryReportGenerator implements ReportGenerator<String> {

    private ProjectGroup reportedGroup;
    private Map<Participant, List<Rating>> participantsRatings;
    private RatingDao ratings = new RatingDao();
    private StringBuilder reportBuilder;

    public SimpleTextProjectGroupRatingsSummaryReportGenerator(ProjectGroup reportedGroup) {
        this.reportedGroup = reportedGroup;
        this.participantsRatings = new HashMap<>();
        this.reportBuilder = new StringBuilder();
    }

    @Override
    public Report<String> generate() {
        populateParticipantsRatingsMap();
        getReportForAllParticipants();
        return new SimpleTextReport(reportBuilder.toString());
    }

    private void populateParticipantsRatingsMap() {
        List<Rating> projectGroupRatings = ratings.findByProjectGroup(reportedGroup);
        for (var rating : projectGroupRatings) {
            participantsRatings.merge(rating.getParticipant(), new LinkedList<>(asList(rating)), (oldVal, val) -> {
                oldVal.addAll(val);
                return oldVal;
            });
        }
    }

    private void getReportForAllParticipants() {
        for (var participantRatings : participantsRatings.entrySet()) {
            reportBuilder.append(getReportForParticipant(participantRatings.getKey(), participantRatings.getValue()));
        }
    }

    private String getReportForParticipant(Participant participant, List<Rating> ratings) {
        RatingAggregator ratingAggregator = new RatingAggregator(ratings);
        StringBuilder sb = new StringBuilder();
        sb.append(participant.getFullName())
                .append("(")
                .append(participant.getIndexNumber())
                .append(", ")
                .append(participant.getEmail())
                .append(")\n");
        for (var rating : ratings) {
            sb.append("    - ")
                    .append(rating.getRatingTitle())
                    .append(": ")
                    .append(rating.getRatingDetails().getRatingValue())
                    .append("/")
                    .append(rating.getRatingDetails().getMaxRatingValue())
                    .append(" (")
                    .append(String.format("%.2f", rating.getRatingDetails().ratingPercentage()))
                    .append("%) - ")
                    .append(rating.getComment())
                    .append("\n");
        }
        sb.append("    RAZEM: ")
                .append(ratingAggregator.sumOfValues())
                .append("/")
                .append(ratingAggregator.sumOfMaxValues())
                .append(" (")
                .append(String.format("%.2f", ratingAggregator.averageValueToMaxRatioPercent()))
                .append("%)\n\n");
        return sb.toString();

    }

}
