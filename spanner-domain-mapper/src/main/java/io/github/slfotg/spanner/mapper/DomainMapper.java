package io.github.slfotg.spanner.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = MappingConstants.ComponentModel.JSR330, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DomainMapper {

    DomainMapper INSTANCE = Mappers.getMapper(DomainMapper.class);

    @Mapping(target = "first", source = "name.first")
    @Mapping(target = "last", source = "name.last")
    io.github.slfotg.spanner.domain.HelloRequest grpcToDomainHelloRequest(
            io.github.slfotg.spanner.grpc.HelloRequest request);

    @Mapping(target = "name.first", source = "first")
    @Mapping(target = "name.last", source = "last")
    io.github.slfotg.spanner.grpc.HelloRequest domainToGrpcHelloRequest(
            io.github.slfotg.spanner.domain.HelloRequest request);

    @Mapping(target = "message", source = "message")
    io.github.slfotg.spanner.domain.HelloReply grpcToDomainHelloReply(
            io.github.slfotg.spanner.grpc.HelloReply reply);

    @Mapping(target = "message", source = "message")
    io.github.slfotg.spanner.grpc.HelloReply domainToGrpcHelloReply(
            io.github.slfotg.spanner.domain.HelloReply reply);

}