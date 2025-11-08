package sistemacine.repository;

import static org.springframework.data.relational.core.query.Criteria.where;
import static org.springframework.data.relational.core.query.Query.query;

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
import org.springframework.data.relational.core.sql.SelectBuilder.SelectFromAndJoin;
import org.springframework.data.relational.core.sql.Table;
import org.springframework.data.relational.repository.support.MappingRelationalEntityInformation;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.r2dbc.core.RowsFetchSpec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import sistemacine.domain.Pelicula;
import sistemacine.domain.Promocion;
import sistemacine.repository.rowmapper.PromocionRowMapper;

/**
 * Spring Data SQL reactive custom repository implementation for the Promocion entity.
 */
@SuppressWarnings("unused")
class PromocionRepositoryInternalImpl extends SimpleR2dbcRepository<Promocion, Long> implements PromocionRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final PromocionRowMapper promocionMapper;

    private static final Table entityTable = Table.aliased("promocion", EntityManager.ENTITY_ALIAS);

    private static final EntityManager.LinkTable peliculasLink = new EntityManager.LinkTable(
        "rel_promocion__peliculas",
        "promocion_id",
        "peliculas_id"
    );

    public PromocionRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        PromocionRowMapper promocionMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(Promocion.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.promocionMapper = promocionMapper;
    }

    @Override
    public Flux<Promocion> findAllBy(Pageable pageable) {
        return findAllBy(pageable, null);
    }

    @Override
    public Flux<Promocion> findAllBy(Pageable pageable, Criteria criteria) {
        return createQuery(pageable, criteria).all();
    }

    RowsFetchSpec<Promocion> createQuery(Pageable pageable, Criteria criteria) {
        List<Expression> columns = PromocionSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        SelectFromAndJoin selectFrom = Select.builder().select(columns).from(entityTable);

        String select = entityManager.createSelect(selectFrom, Promocion.class, pageable, criteria);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<Promocion> findAll() {
        return findAllBy(null, null);
    }

    @Override
    public Mono<Promocion> findById(Long id) {
        return createQuery(null, where(EntityManager.ENTITY_ALIAS + ".id").is(id)).one();
    }

    @Override
    public Mono<Promocion> findOneWithEagerRelationships(Long id) {
        return findById(id);
    }

    @Override
    public Flux<Promocion> findAllWithEagerRelationships() {
        return findAll();
    }

    @Override
    public Flux<Promocion> findAllWithEagerRelationships(Pageable page) {
        return findAllBy(page);
    }

    private Promocion process(Row row, RowMetadata metadata) {
        Promocion entity = promocionMapper.apply(row, "e");
        return entity;
    }

    @Override
    public <S extends Promocion> Mono<S> save(S entity) {
        return super.save(entity).flatMap((S e) -> updateRelations(e));
    }

    protected <S extends Promocion> Mono<S> updateRelations(S entity) {
        Mono<Void> result = entityManager
            .updateLinkTable(peliculasLink, entity.getId(), entity.getPeliculas().stream().map(Pelicula::getId))
            .then();
        return result.thenReturn(entity);
    }

    @Override
    public Mono<Void> deleteById(Long entityId) {
        return deleteRelations(entityId).then(super.deleteById(entityId));
    }

    protected Mono<Void> deleteRelations(Long entityId) {
        return entityManager.deleteFromLinkTable(peliculasLink, entityId);
    }
}
