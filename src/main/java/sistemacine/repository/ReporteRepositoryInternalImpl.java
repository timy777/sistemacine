package sistemacine.repository;

import static org.springframework.data.relational.core.query.Criteria.where;

import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import java.time.LocalDate;
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
import sistemacine.domain.Reporte;
import sistemacine.repository.rowmapper.PersonaRowMapper;
import sistemacine.repository.rowmapper.ReporteRowMapper;

/**
 * Spring Data SQL reactive custom repository implementation for the Reporte entity.
 */
@SuppressWarnings("unused")
class ReporteRepositoryInternalImpl extends SimpleR2dbcRepository<Reporte, Long> implements ReporteRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final PersonaRowMapper personaMapper;
    private final ReporteRowMapper reporteMapper;

    private static final Table entityTable = Table.aliased("reporte", EntityManager.ENTITY_ALIAS);
    private static final Table vendedorTable = Table.aliased("persona", "vendedor");

    public ReporteRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        PersonaRowMapper personaMapper,
        ReporteRowMapper reporteMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(Reporte.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.personaMapper = personaMapper;
        this.reporteMapper = reporteMapper;
    }

    @Override
    public Flux<Reporte> findAllBy(Pageable pageable) {
        return findAllBy(pageable, null);
    }

    @Override
    public Flux<Reporte> findAllBy(Pageable pageable, Criteria criteria) {
        return createQuery(pageable, criteria).all();
    }

    RowsFetchSpec<Reporte> createQuery(Pageable pageable, Criteria criteria) {
        List<Expression> columns = ReporteSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(PersonaSqlHelper.getColumns(vendedorTable, "vendedor"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(vendedorTable)
            .on(Column.create("vendedor_id", entityTable))
            .equals(Column.create("id", vendedorTable));

        String select = entityManager.createSelect(selectFrom, Reporte.class, pageable, criteria);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<Reporte> findAll() {
        return findAllBy(null, null);
    }

    @Override
    public Mono<Reporte> findById(Long id) {
        return createQuery(null, where(EntityManager.ENTITY_ALIAS + ".id").is(id)).one();
    }

    @Override
    public Mono<Reporte> findOneWithEagerRelationships(Long id) {
        return findById(id);
    }

    @Override
    public Flux<Reporte> findAllWithEagerRelationships() {
        return findAll();
    }

    @Override
    public Flux<Reporte> findAllWithEagerRelationships(Pageable page) {
        return findAllBy(page);
    }

    private Reporte process(Row row, RowMetadata metadata) {
        Reporte entity = reporteMapper.apply(row, "e");
        entity.setVendedor(personaMapper.apply(row, "vendedor"));
        return entity;
    }

    @Override
    public <S extends Reporte> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
