package sistemacine.repository;

import static org.springframework.data.relational.core.query.Criteria.where;

import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import java.math.BigDecimal;
import java.time.Instant;
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
import sistemacine.domain.Funcion;
import sistemacine.repository.rowmapper.FuncionRowMapper;
import sistemacine.repository.rowmapper.PeliculaRowMapper;
import sistemacine.repository.rowmapper.SalaRowMapper;
import sistemacine.repository.rowmapper.TarifaRowMapper;

/**
 * Spring Data SQL reactive custom repository implementation for the Funcion entity.
 */
@SuppressWarnings("unused")
class FuncionRepositoryInternalImpl extends SimpleR2dbcRepository<Funcion, Long> implements FuncionRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final SalaRowMapper salaMapper;
    private final PeliculaRowMapper peliculaMapper;
    private final TarifaRowMapper tarifaMapper;
    private final FuncionRowMapper funcionMapper;

    private static final Table entityTable = Table.aliased("funcion", EntityManager.ENTITY_ALIAS);
    private static final Table salaTable = Table.aliased("sala", "sala");
    private static final Table peliculaTable = Table.aliased("pelicula", "pelicula");
    private static final Table tarifaTable = Table.aliased("tarifa", "tarifa");

    public FuncionRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        SalaRowMapper salaMapper,
        PeliculaRowMapper peliculaMapper,
        TarifaRowMapper tarifaMapper,
        FuncionRowMapper funcionMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(Funcion.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.salaMapper = salaMapper;
        this.peliculaMapper = peliculaMapper;
        this.tarifaMapper = tarifaMapper;
        this.funcionMapper = funcionMapper;
    }

    @Override
    public Flux<Funcion> findAllBy(Pageable pageable) {
        return findAllBy(pageable, null);
    }

    @Override
    public Flux<Funcion> findAllBy(Pageable pageable, Criteria criteria) {
        return createQuery(pageable, criteria).all();
    }

    RowsFetchSpec<Funcion> createQuery(Pageable pageable, Criteria criteria) {
        List<Expression> columns = FuncionSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(SalaSqlHelper.getColumns(salaTable, "sala"));
        columns.addAll(PeliculaSqlHelper.getColumns(peliculaTable, "pelicula"));
        columns.addAll(TarifaSqlHelper.getColumns(tarifaTable, "tarifa"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(salaTable)
            .on(Column.create("sala_id", entityTable))
            .equals(Column.create("id", salaTable))
            .leftOuterJoin(peliculaTable)
            .on(Column.create("pelicula_id", entityTable))
            .equals(Column.create("id", peliculaTable))
            .leftOuterJoin(tarifaTable)
            .on(Column.create("tarifa_id", entityTable))
            .equals(Column.create("id", tarifaTable));

        String select = entityManager.createSelect(selectFrom, Funcion.class, pageable, criteria);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<Funcion> findAll() {
        return findAllBy(null, null);
    }

    @Override
    public Mono<Funcion> findById(Long id) {
        return createQuery(null, where(EntityManager.ENTITY_ALIAS + ".id").is(id)).one();
    }

    @Override
    public Mono<Funcion> findOneWithEagerRelationships(Long id) {
        return findById(id);
    }

    @Override
    public Flux<Funcion> findAllWithEagerRelationships() {
        return findAll();
    }

    @Override
    public Flux<Funcion> findAllWithEagerRelationships(Pageable page) {
        return findAllBy(page);
    }

    private Funcion process(Row row, RowMetadata metadata) {
        Funcion entity = funcionMapper.apply(row, "e");
        entity.setSala(salaMapper.apply(row, "sala"));
        entity.setPelicula(peliculaMapper.apply(row, "pelicula"));
        entity.setTarifa(tarifaMapper.apply(row, "tarifa"));
        return entity;
    }

    @Override
    public <S extends Funcion> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
