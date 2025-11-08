package sistemacine.repository;

import static org.springframework.data.relational.core.query.Criteria.where;

import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.function.BiFunction;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.convert.R2dbcConverter;
import org.springframework.data.r2dbc.core.R2dbcEntityOperations;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.repository.support.SimpleR2dbcRepository;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Select;
import org.springframework.data.relational.core.sql.SelectBuilder.SelectFromAndJoinCondition;
import org.springframework.data.relational.core.sql.Table;
import org.springframework.data.relational.repository.support.MappingRelationalEntityInformation;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.r2dbc.core.RowsFetchSpec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import sistemacine.domain.DetalleVenta;
import sistemacine.repository.rowmapper.DetalleVentaRowMapper;
import sistemacine.repository.rowmapper.FuncionRowMapper;
import sistemacine.repository.rowmapper.VentaRowMapper;

/**
 * Spring Data SQL reactive custom repository implementation for the DetalleVenta entity.
 */
@SuppressWarnings("unused")
class DetalleVentaRepositoryInternalImpl extends SimpleR2dbcRepository<DetalleVenta, Long> implements DetalleVentaRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final FuncionRowMapper funcionMapper;
    private final VentaRowMapper ventaMapper;
    private final DetalleVentaRowMapper detalleventaMapper;

    private static final Table entityTable = Table.aliased("detalle_venta", EntityManager.ENTITY_ALIAS);
    private static final Table funcionTable = Table.aliased("funcion", "funcion");
    private static final Table ventaTable = Table.aliased("venta", "venta");

    public DetalleVentaRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        FuncionRowMapper funcionMapper,
        VentaRowMapper ventaMapper,
        DetalleVentaRowMapper detalleventaMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(DetalleVenta.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.funcionMapper = funcionMapper;
        this.ventaMapper = ventaMapper;
        this.detalleventaMapper = detalleventaMapper;
    }

    @Override
    public Flux<DetalleVenta> findAllBy(Pageable pageable) {
        return findAllBy(pageable, null);
    }

    @Override
    public Flux<DetalleVenta> findAllBy(Pageable pageable, Criteria criteria) {
        return createQuery(pageable, criteria).all();
    }

    RowsFetchSpec<DetalleVenta> createQuery(Pageable pageable, Criteria criteria) {
        List<Expression> columns = DetalleVentaSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(FuncionSqlHelper.getColumns(funcionTable, "funcion"));
        columns.addAll(VentaSqlHelper.getColumns(ventaTable, "venta"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(funcionTable)
            .on(Column.create("funcion_id", entityTable))
            .equals(Column.create("id", funcionTable))
            .leftOuterJoin(ventaTable)
            .on(Column.create("venta_id", entityTable))
            .equals(Column.create("id", ventaTable));

        String select = entityManager.createSelect(selectFrom, DetalleVenta.class, pageable, criteria);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<DetalleVenta> findAll() {
        return findAllBy(null, null);
    }

    @Override
    public Mono<DetalleVenta> findById(Long id) {
        return createQuery(null, where(EntityManager.ENTITY_ALIAS + ".id").is(id)).one();
    }

    private DetalleVenta process(Row row, RowMetadata metadata) {
        DetalleVenta entity = detalleventaMapper.apply(row, "e");
        entity.setFuncion(funcionMapper.apply(row, "funcion"));
        entity.setVenta(ventaMapper.apply(row, "venta"));
        return entity;
    }

    @Override
    public <S extends DetalleVenta> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
