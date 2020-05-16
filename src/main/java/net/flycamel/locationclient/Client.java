package net.flycamel.locationclient;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.locationservice.LocationServiceGrpc;
import io.grpc.locationservice.LocationServiceGrpc.LocationServiceBlockingStub;
import io.grpc.locationservice.LocationServiceOuterClass.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Client {
    void start() {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9999)
                .usePlaintext()
                .build();

        LocationServiceBlockingStub stub = LocationServiceGrpc.newBlockingStub(channel);

        log.info("위치 저장/검색 서비스 테스트 클라이언트");

        log.info("------------------------------");
        log.info("put rpc 테스트1");
        PutRequest request = PutRequest.newBuilder()
                .setId("100")
                .setLatitude(38.456)
                .setLongitude(120.123)
                .build();

        log.info("Send Request : {}", request);
        CommonLocationInfo response = stub.put(request);
        log.info("Received Response : {}", response);

        log.info("put rpc 테스트2");
        request = PutRequest.newBuilder()
                .setId("200")
                .setLatitude(39.456)
                .setLongitude(121.123)
                .build();

        log.info("Send Request : {}", request);
        response = stub.put(request);
        log.info("Received Response : {}", response);

        log.info("put rpc 테스트3");
        request = PutRequest.newBuilder()
                .setId("100")
                .setLatitude(39.123)
                .setLongitude(120.456)
                .build();

        log.info("Send Request : {}", request);
        response = stub.put(request);
        log.info("Received Response : {}", response);

        log.info("------------------------------");
        log.info("get rpc 테스트");
        log.info("100번은 두 번 put 되었고, 두번째 항목이 와야함");
        GetRequest getRequest = GetRequest.newBuilder()
                .setId("100")
                .build();

        log.info("Send GetRequest : {}", getRequest);
        CommonLocationInfo getResponse = stub.get(getRequest);
        log.info("Received GetResponse : {}", getResponse);

        log.info("------------------------------");
        log.info("3초간 sleep");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("------------------------------");
        log.info("100번에 대해 2건 put");
        request = PutRequest.newBuilder()
                .setId("100")
                .setLatitude(39.123)
                .setLongitude(120.456)
                .build();

        stub.put(request);

        request = PutRequest.newBuilder()
                .setId("100")
                .setLatitude(39.456)
                .setLongitude(120.123)
                .build();

        stub.put(request);

        log.info("------------------------------");
        log.info("history rpc 테스트");
        log.info("100번은 네 번 put 되었고, 시작을 0, 끝을 Long.Max_VALUE로 했으므로 4건 이상 나와야함");
        HistoryRequest historyRequest = HistoryRequest.newBuilder()
                .setId("100")
                .setStartTime(Long.MIN_VALUE)
                .setEndTime(Long.MAX_VALUE)
                .build();

        log.info("Send HistoryRequest : {}", historyRequest);
        HistoryResponse historyResponse = stub.history(historyRequest);
        log.info("Received HistoryResponse : {}", historyResponse);

        log.info("history rpc 테스트2");
        log.info("100번은 두 번 put 되었고, 시작을 3초전, 끝을 Long.Max_VALUE로 했으므로 최근 2건 나와야함");
        historyRequest = HistoryRequest.newBuilder()
                .setId("100")
                .setStartTime(System.currentTimeMillis() - 3000L)
                .setEndTime(Long.MAX_VALUE)
                .build();

        log.info("Send HistoryRequest : {}", historyRequest);
        historyResponse = stub.history(historyRequest);
        log.info("Received HistoryResponse : {}", historyResponse);

        log.info("------------------------------");
        log.info("search rpc 테스트1");
        SearchRequest searchRequest = SearchRequest.newBuilder()
                .setLatitude(38.0)
                .setLongitude(120.0)
                .setRadius(170.0)
                .build();

        log.info("Send SearchRequest : {}", searchRequest);
        SearchResponse searchResponse = stub.search(searchRequest);
        log.info("Received SearchResponse : {}", searchResponse);

        log.info("search rpc 테스트2");
        searchRequest = SearchRequest.newBuilder()
                .setLatitude(38.0)
                .setLongitude(120.0)
                .setRadius(500.0)
                .build();

        log.info("Send SearchRequest : {}", searchRequest);
        searchResponse = stub.search(searchRequest);
        log.info("Received SearchResponse : {}", searchResponse);
    }
}
