package sistemacine.repository;

import static org.springframework.data.relational.core.query.Criteria.where;

import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import java.math.BigDecimal;
import java.time.Instant;
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
import sistemacine.domain.Venta;
import sistemacine.repository.rowmapper.PersonaRowMapper;
import sistemacine.repository.rowmapper.VentaRowMapper;

/**
 * Spring Data SQL reactive custom repository implementation for the Venta entity.
 */
@SuppressWarnings("unused")
class VentaRepositoryInternalImpl extends SimpleR2dbcRepository<Venta, Long> implements VentaRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final PersonaRowMapper personaMapper;
    private final VentaRowMapper ventaMapper;

    private static final Table entityTable = Table.aliased("venta", EntityManager.ENTITY_ALIAS);
    private static final Table clienteTable = Table.aliased("persona", "cliente");
    private static final Table vendedorTable = Table.aliased("persona", "vendedor");

    public VentaRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        PersonaRowMapper personaMapper,
        VentaRowMapper ventaMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(Venta.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.personaMapper = personaMapper;
        this.ventaMapper = ventaMapper;
    }

    @Override
    public Flux<Venta> findAllBy(Pageable pageable) {
        return findAllBy(pageable, null);
    }

    @Override
    public Flux<Venta> findAllBy(Pageable pageable, Criteria criteria) {
        return createQuery(pageable, criteria).all();
    }

    RowsFetchSpec<Venta> createQuery(Pageable pageable, Criteria criteria) {
        List<Expression> columns = VentaSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(PersonaSqlHelper.getColumns(clienteTable, "cliente"));
        columns.addAll(PersonaSqlHelper.getColumns(vendedorTable, "vendedor"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(clienteTable)
            .on(Column.create("cliente_id", entityTable))
            .equals(Column.create("id", clienteTable))
            .leftOuterJoin(vendedorTable)
            .on(Column.create("vendedor_id", entityTable))
            .equals(Column.create("id", vendedorTable));

        String select = entityManager.createSelect(selectFrom, Venta.class, pageable, criteria);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<Venta> findAll() {
        return findAllBy(null, null);
    }

    @Override
    public Mono<Venta> findById(Long id) {
        return createQuery(null, where(EntityManager.ENTITY_ALIAS + ".id").is(id)).one();
    }

    @Override
    public Mono<Venta> findOneWithEagerRelationships(Long id) {
        return findById(id);
    }

    @Override
    public Flux<Venta> findAllWithEagerRelationships() {
        return findAll();
    }

    @Override
    public Flux<Venta> findAllWithEagerRelationships(Pageable page) {
        return findAllBy(page);
    }

    private Venta process(Row row, RowMetadata metadata) {
        Venta entity = ventaMapper.apply(row, "e");
        entity.setCliente(personaMapper.apply(row, "cliente"));
        entity.setVendedor(personaMapper.apply(row, "vendedor"));
        return entity;
    }

    @Override
    public <S extends Venta> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
