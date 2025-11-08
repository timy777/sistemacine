package sistemacine.repository;

import static org.springframework.data.relational.core.query.Criteria.where;

import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
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
import sistemacine.domain.Pelicula;
import sistemacine.domain.enumeration.EstadoPelicula;
import sistemacine.repository.rowmapper.GeneroRowMapper;
import sistemacine.repository.rowmapper.PeliculaRowMapper;

/**
 * Spring Data SQL reactive custom repository implementation for the Pelicula entity.
 */
@SuppressWarnings("unused")
class PeliculaRepositoryInternalImpl extends SimpleR2dbcRepository<Pelicula, Long> implements PeliculaRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final GeneroRowMapper generoMapper;
    private final PeliculaRowMapper peliculaMapper;

    private static final Table entityTable = Table.aliased("pelicula", EntityManager.ENTITY_ALIAS);
    private static final Table generoTable = Table.aliased("genero", "genero");

    public PeliculaRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        GeneroRowMapper generoMapper,
        PeliculaRowMapper peliculaMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(Pelicula.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.generoMapper = generoMapper;
        this.peliculaMapper = peliculaMapper;
    }

    @Override
    public Flux<Pelicula> findAllBy(Pageable pageable) {
        return findAllBy(pageable, null);
    }

    @Override
    public Flux<Pelicula> findAllBy(Pageable pageable, Criteria criteria) {
        return createQuery(pageable, criteria).all();
    }

    RowsFetchSpec<Pelicula> createQuery(Pageable pageable, Criteria criteria) {
        List<Expression> columns = PeliculaSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(GeneroSqlHelper.getColumns(generoTable, "genero"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(generoTable)
            .on(Column.create("genero_id", entityTable))
            .equals(Column.create("id", generoTable));

        String select = entityManager.createSelect(selectFrom, Pelicula.class, pageable, criteria);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<Pelicula> findAll() {
        return findAllBy(null, null);
    }

    @Override
    public Mono<Pelicula> findById(Long id) {
        return createQuery(null, where(EntityManager.ENTITY_ALIAS + ".id").is(id)).one();
    }

    @Override
    public Mono<Pelicula> findOneWithEagerRelationships(Long id) {
        return findById(id);
    }

    @Override
    public Flux<Pelicula> findAllWithEagerRelationships() {
        return findAll();
    }

    @Override
    public Flux<Pelicula> findAllWithEagerRelationships(Pageable page) {
        return findAllBy(page);
    }

    private Pelicula process(Row row, RowMetadata metadata) {
        Pelicula entity = peliculaMapper.apply(row, "e");
        entity.setGenero(generoMapper.apply(row, "genero"));
        return entity;
    }

    @Override
    public <S extends Pelicula> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
