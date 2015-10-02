package com.silanis.esl.sdk.examples;

import au.com.bytecode.opencsv.CSVReader;
import com.silanis.esl.sdk.*;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;

/**
 * Created by lena on 2014-05-27.
 */
public class DownloadReportExampleTest {

    @Test
    synchronized public void verifyResult() throws IOException {
        DownloadReportExample example = new DownloadReportExample(Props.get());
        example.run();
        // Assert correct download of completion report for a sender
        CompletionReport completionReportForSender = example.sdkCompletionReportForSenderDraft;
        SenderCompletionReport senderCompletionReportForSenderId1 = getSenderCompletionReportForSenderId(example.sdkCompletionReportForSenderDraft.getSenders(), example.senderUID);

        assertThat("There should be only 1 sender.", completionReportForSender.getSenders().size(), is(1));
        assertThat("Number of package completion reports should be greater than 1.", senderCompletionReportForSenderId1.getPackages().size(), is(greaterThanOrEqualTo(1)));
        assertThat("Number of document completion report should be greater than 1.", senderCompletionReportForSenderId1.getPackages().get(0).getDocuments().size(), is(greaterThanOrEqualTo(1)));
        assertThat("Number of signer completion report should be greater than 1.", senderCompletionReportForSenderId1.getPackages().get(0).getSigners().size(), is(greaterThanOrEqualTo(1)));

        assertCreatedPackageIncludedInCompletionReport(completionReportForSender, example.senderUID, example.packageId, "DRAFT");
        assertThat("Cannot download the completion report in csv format.", example.csvCompletionReportForSenderDraft, is(not(isEmptyOrNullString())));

        CSVReader reader = new CSVReader(new InputStreamReader(new ByteArrayInputStream(example.csvCompletionReportForSenderDraft.getBytes())));
        List<String[]> rows = reader.readAll();

        if(senderCompletionReportForSenderId1.getPackages().size() > 0) {
            assertThat(rows, hasSize(greaterThanOrEqualTo(senderCompletionReportForSenderId1.getPackages().size() - 1)));
            assertThat(rows, hasSize(lessThanOrEqualTo(senderCompletionReportForSenderId1.getPackages().size() + 3)));
        }

        assertCreatedPackageIncludedInCSV(rows, example.packageId, "DRAFT");

        completionReportForSender = example.sdkCompletionReportForSenderSent;
        SenderCompletionReport senderCompletionReportForSenderId3 = getSenderCompletionReportForSenderId(example.sdkCompletionReportForSenderSent.getSenders(), example.senderUID);

        assertThat("There should be only 1 sender.", completionReportForSender.getSenders().size(), is(1));
        assertThat("Number of package completion reports should be greater than 1.", senderCompletionReportForSenderId3.getPackages().size(), is(greaterThanOrEqualTo(1)));
        assertThat("Number of document completion report should be greater than 1.", senderCompletionReportForSenderId3.getPackages().get(0).getDocuments().size(), is(greaterThanOrEqualTo(1)));
        assertThat("Number of signer completion report should be greater than 1.", senderCompletionReportForSenderId3.getPackages().get(0).getSigners().size(), is(greaterThanOrEqualTo(1)));

        assertCreatedPackageIncludedInCompletionReport(completionReportForSender, example.senderUID, example.package2Id, "SENT");

        assertThat("Cannot download the completion report in csv format.", example.csvCompletionReportForSenderSent, is(not(isEmptyOrNullString())));

        reader = new CSVReader(new InputStreamReader(new ByteArrayInputStream(example.csvCompletionReportForSenderSent.getBytes())));
        rows = reader.readAll();

        if(senderCompletionReportForSenderId3.getPackages().size() > 0) {
            assertThat(rows, hasSize(greaterThanOrEqualTo(senderCompletionReportForSenderId3.getPackages().size() - 1)));
            assertThat(rows, hasSize(lessThanOrEqualTo(senderCompletionReportForSenderId3.getPackages().size() + 3)));
        }

        assertCreatedPackageIncludedInCSV(rows, example.package2Id, "SENT");

        // Assert correct download of completion report for all senders
        CompletionReport completionReport = example.sdkCompletionReportDraft;
        SenderCompletionReport senderCompletionReportForSenderId2 = getSenderCompletionReportForSenderId(completionReport.getSenders(), example.senderUID);

        assertThat("Number of sender should be greater than 1.", completionReport.getSenders().size(), is(greaterThanOrEqualTo(1)));
        assertThat("Number of package completion reports should be greater than 0.", senderCompletionReportForSenderId2.getPackages().size(), is(greaterThanOrEqualTo(0)));

        assertCreatedPackageIncludedInCompletionReport(completionReport, example.senderUID, example.packageId, "DRAFT");

        assertThat("Cannot download the completion report in csv format.", example.csvCompletionReportDraft, is(not(isEmptyOrNullString())));

        reader = new CSVReader(new InputStreamReader(new ByteArrayInputStream(example.csvCompletionReportDraft.getBytes())));
        rows = reader.readAll();

        if(senderCompletionReportForSenderId2.getPackages().size() > 0) {
            assertThat(rows, hasSize(greaterThanOrEqualTo(getCompletionReportCount(completionReport) - 1)));
            assertThat(rows, hasSize(lessThanOrEqualTo(getCompletionReportCount(completionReport) + 3)));
        }

        assertCreatedPackageIncludedInCSV(rows, example.packageId, "DRAFT");

        completionReport = example.sdkCompletionReportSent;
        assertThat("Number of sender should be greater than 1.", completionReport.getSenders().size(), is(greaterThanOrEqualTo(1)));
        assertThat("Number of package completion reports should be greater than 0.", senderCompletionReportForSenderId2.getPackages().size(), is(greaterThanOrEqualTo(0)));

        assertCreatedPackageIncludedInCompletionReport(completionReport, example.senderUID, example.package2Id, "SENT");

        assertThat("Cannot download the completion report in csv format.", example.csvCompletionReportSent, is(not(isEmptyOrNullString())));

        reader = new CSVReader(new InputStreamReader(new ByteArrayInputStream(example.csvCompletionReportSent.getBytes())));
        rows = reader.readAll();

        if(senderCompletionReportForSenderId2.getPackages().size() > 0) {
            assertThat(rows, hasSize(greaterThanOrEqualTo(getCompletionReportCount(completionReport) - 1)));
            assertThat(rows, hasSize(lessThanOrEqualTo(getCompletionReportCount(completionReport) + 3)));
        }

        assertCreatedPackageIncludedInCSV(rows, example.package2Id, "SENT");

        // Assert correct download of usage report
        UsageReport usageReport = example.sdkUsageReport;
        SenderUsageReport senderUsageReportForSenderId = getSenderUsageReportForSenderId(usageReport.getSenderUsageReports(), example.senderUID);

        assertThat("There should be only 1 sender.", usageReport.getSenderUsageReports().size(), is(greaterThanOrEqualTo(1)));
        assertThat("Number of map entries should be greater or equal to 1.", senderUsageReportForSenderId.getCountByUsageReportCategory().size(), is(greaterThanOrEqualTo(1)));
        assertThat("There should be at a draft key in packages map.", senderUsageReportForSenderId.getCountByUsageReportCategory().containsKey(UsageReportCategory.DRAFT), is(true));
        assertThat("Number of drafts should be greater or equal to 1.", senderUsageReportForSenderId.getCountByUsageReportCategory().get(UsageReportCategory.DRAFT), is(greaterThanOrEqualTo(1)));

        assertThat("Cannot download the usage report in csv format.", example.csvUsageReport, is(not(isEmptyOrNullString())));

        reader = new CSVReader(new InputStreamReader(new ByteArrayInputStream(example.csvUsageReport.getBytes())));
        rows = reader.readAll();

        if(usageReport.getSenderUsageReports().size() > 0) {
            assertThat(rows, hasSize(greaterThanOrEqualTo(usageReport.getSenderUsageReports().size() - 1)));
            assertThat(rows, hasSize(lessThanOrEqualTo(usageReport.getSenderUsageReports().size() + 3)));
        }

        // Assert correct download of delegation report
        DelegationReport delegationReportForAccountWithoutDate = example.sdkDelegationReportForAccountWithoutDate;
        assertThat("Number of delegation event reports should be equal or greater than 0.", delegationReportForAccountWithoutDate.getDelegationEventReports().size(), is(greaterThanOrEqualTo(0)));

        assertThat("Cannot download the delegation report in csv format.", example.csvDelegationReportForAccountWithoutDate, is(not(isEmptyOrNullString())));

        reader = new CSVReader(new InputStreamReader(new ByteArrayInputStream(example.csvDelegationReportForAccountWithoutDate.getBytes())));
        rows = reader.readAll();

        if(delegationReportForAccountWithoutDate.getDelegationEventReports().size() > 0) {
            rows = getRowsBySender(rows, example.senderUID);
            assertThat(rows, hasSize(delegationReportForAccountWithoutDate.getDelegationEventReports().get(example.senderUID).size()));
        }

        DelegationReport delegationReportForAccount = example.sdkDelegationReportForAccount;
        assertThat("Number of delegation event reports should be equal or greater than 0.", delegationReportForAccount.getDelegationEventReports().size(), is(greaterThanOrEqualTo(0)));

        assertThat("Cannot download the delegation report in csv format.", example.csvDelegationReportForAccount, is(not(isEmptyOrNullString())));

        reader = new CSVReader(new InputStreamReader(new ByteArrayInputStream(example.csvDelegationReportForAccount.getBytes())));
        rows = reader.readAll();

        if(delegationReportForAccount.getDelegationEventReports().size() > 0) {
            rows = getRowsBySender(rows, example.senderUID);
            assertThat(rows, hasSize(delegationReportForAccount.getDelegationEventReports().get(example.senderUID).size()));
        }

        DelegationReport delegationReportForSender = example.sdkDelegationReportForSender;
        assertThat("Number of delegation event reports should be equal or greater than 0.", delegationReportForSender.getDelegationEventReports().size(), is(greaterThanOrEqualTo(0)));

        assertThat("Cannot download the delegation report in csv format.", example.csvDelegationReportForSender, is(not(isEmptyOrNullString())));

        reader = new CSVReader(new InputStreamReader(new ByteArrayInputStream(example.csvDelegationReportForSender.getBytes())));
        rows = reader.readAll();

        if(delegationReportForAccount.getDelegationEventReports().size() > 0) {
            rows = getRowsBySender(rows, example.senderUID);
            assertThat(rows, hasSize(delegationReportForAccount.getDelegationEventReports().get(example.senderUID).size()));
        }
    }

    private SenderCompletionReport getSenderCompletionReportForSenderId(List<SenderCompletionReport> senderCompletionReports, String senderId) {
        for (SenderCompletionReport senderCompletionReport : senderCompletionReports) {
            if (StringUtils.equals(senderId, senderCompletionReport.getSender().getId())) {
                return senderCompletionReport;
            }

        }
        throw new RuntimeException("Could not find SenderCompletionReport for SenderId " + senderId);
    }

    private SenderUsageReport getSenderUsageReportForSenderId(List<SenderUsageReport> senderUsageReports, String senderId) {
        for (SenderUsageReport senderUsageReport : senderUsageReports) {
            if (StringUtils.equals(senderId, senderUsageReport.getSender().getId())) {
                return senderUsageReport;
            }

        }
        throw new RuntimeException("Could not find SenderUsageReport for SenderId " + senderId);
    }

    private int getCompletionReportCount(CompletionReport completionReport) {
        int count = 0;
        for(SenderCompletionReport senderCompletionReport : completionReport.getSenders()) {
            count += senderCompletionReport.getPackages().size();
        }
        return count;
    }

    private void assertCreatedPackageIncludedInCompletionReport(CompletionReport completionReport, String sender, PackageId packageId, String packageStatus) {
        PackageCompletionReport createdPackageCompletionReport = getCreatedPackageCompletionReport(completionReport, sender, packageId);

        assertThat(createdPackageCompletionReport, is(notNullValue()));
        assertThat(createdPackageCompletionReport.getPackageStatus(), is(notNullValue()));
        assertThat(createdPackageCompletionReport.getPackageStatus().name(), is(packageStatus));
    }

    private void assertCreatedPackageIncludedInCSV(List<String[]> rows, PackageId packageId, String packageStatus) {
        String[] createdPackageRow = getCreatedPackageCSVRow(rows, packageId);
        assertThat(createdPackageRow, is(notNullValue()));
        assertThat(Arrays.asList(createdPackageRow), hasItems(packageId.getId(), packageStatus));
    }

    private PackageCompletionReport getCreatedPackageCompletionReport(CompletionReport completionReport, String sender, PackageId packageId) {
        SenderCompletionReport senderCompletionReport = getSenderCompletionReport(completionReport, sender);

        List<PackageCompletionReport> packageCompletionReports = senderCompletionReport.getPackages();
        for(PackageCompletionReport packageCompletionReport : packageCompletionReports) {
            if(packageCompletionReport.getId().equals(packageId.getId())) {
                return packageCompletionReport;
            }
        }
        return null;
    }

    private SenderCompletionReport getSenderCompletionReport(CompletionReport completionReport, String sender) {
        for(SenderCompletionReport senderCompletionReport : completionReport.getSenders()) {
            if(senderCompletionReport.getSender().getId().equals(sender)) {
                return senderCompletionReport;
            }
        }
        return null;
    }

    private String[] getCreatedPackageCSVRow(List<String[]> rows, PackageId packageId) {
        for(String[] row : rows) {
            for(String word: row) {
                if(word.contains(packageId.getId())) {
                    return row;
                }
            }
        }
        return null;
    }

    private List<String[]> getRowsBySender(List<String[]> rows, String sender) {
        List<String[]> result = new ArrayList<String[]>();
        for(String[] row : rows) {
            for(String word: row) {
                if(word.contains(sender)) {
                    result.add(row);
                    break;
                }
            }
        }
        return result;
    }
}