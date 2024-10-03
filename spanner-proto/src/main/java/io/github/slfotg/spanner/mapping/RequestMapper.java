package io.github.slfotg.spanner.mapping;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = MappingConstants.ComponentModel.JSR330, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RequestMapper {

    RequestMapper INSTANCE = Mappers.getMapper(RequestMapper.class);

    /**
     * Maps a request.
     *
     * @param request the domain request
     * @return the grpc request
     */
    @Mapping(target = "name.first", source = "first")
    @Mapping(target = "name.last", source = "last")
    io.github.slfotg.spanner.grpc.HelloRequest toHelloRequest(
            io.github.slfotg.spanner.domain.HelloRequest request);
}
