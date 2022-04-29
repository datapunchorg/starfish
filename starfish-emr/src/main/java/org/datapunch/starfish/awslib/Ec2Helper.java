package org.datapunch.starfish.awslib;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ec2.Ec2Client;
import software.amazon.awssdk.services.ec2.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * To run this Java V2 code example, ensure that you have setup your development environment, including your credentials.
 *
 * For information, see this documentation topic:
 *
 * https://docs.aws.amazon.com/sdk-for-java/latest/developer-guide/get-started.html
 */
public class Ec2Helper {

    public static List<String> getVpcIds(String region) {
        try (Ec2Client ec2 = Ec2Client.builder()
                .region(Region.of(region))
                .build()) {
            DescribeVpcsResponse describeVpcsResponse = ec2.describeVpcs();
            return describeVpcsResponse.vpcs().stream().map(t -> t.vpcId()).collect(Collectors.toList());
        }
    }

    public static List<String> getSubnetIds(String region) {
        List<String> result = new ArrayList<>();
        try (Ec2Client ec2 = Ec2Client.builder()
                .region(Region.of(region))
                .build()) {
            DescribeVpcsResponse describeVpcsResponse = ec2.describeVpcs();
            describeVpcsResponse.vpcs().stream().map(t -> t.vpcId())
                    .forEach(t -> {
                        String nextToken = null;
                        boolean hasNext = true;
                        while (hasNext) {
                            DescribeSubnetsRequest describeSubnetsRequest =
                                    DescribeSubnetsRequest.builder().nextToken(nextToken).build();
                            DescribeSubnetsResponse describeSubnetsResponse = ec2.describeSubnets(describeSubnetsRequest);
                            nextToken = describeSubnetsResponse.nextToken();
                            hasNext = nextToken != null;
                            describeSubnetsResponse.subnets().stream().forEach(subnet -> {
                                result.add(subnet.subnetId());
                            });
                        }
                    });
        }
        return result;
    }

    public static void main(String[] args) {
        getSubnetIds("us-west-1");
    }

    public static void describeEC2Vpcs(Ec2Client ec2, String vpcId) {

        try {
            DescribeVpcsRequest request = DescribeVpcsRequest.builder()
                    .vpcIds(vpcId)
                    .build();

            DescribeVpcsResponse response =
                    ec2.describeVpcs(request);

            for (Vpc vpc : response.vpcs()) {
                System.out.printf(
                        "Found VPC with id %s, " +
                                "vpc state %s " +
                                "and tennancy %s",
                        vpc.vpcId(),
                        vpc.stateAsString(),
                        vpc.instanceTenancyAsString());
            }
        } catch (Ec2Exception e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }
}
