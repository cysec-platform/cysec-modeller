create table category (
    id   BIGINT auto_increment primary key,
    name VARCHAR(255)
);

create table control (
    id          BIGINT auto_increment primary key,
    key         VARCHAR(255) UNIQUE,
    name        VARCHAR(511),
    details     VARCHAR(2000),
    category_id BIGINT,
    constraint fk_control_category_id_category_id
        foreign key (category_id) references category (id)
);

create table control_dependencies (
    control_id            BIGINT not null,
    dependency_control_id BIGINT not null,
    constraint fk_control_dependencies_control_id_control_id
        foreign key (control_id) references control (id),
    constraint fk_control_dependencies_dependency_control_id_control_id
        foreign key (dependency_control_id) references control (id)
);

create table external_source (
    id   BIGINT auto_increment primary key,
    key  VARCHAR(255) UNIQUE,
    name VARCHAR(255)
);

create table external_control (
    id        BIGINT auto_increment primary key,
    key       VARCHAR(255) UNIQUE,
    name      VARCHAR(1000),
    details   VARCHAR(2000),
    source_id BIGINT,
    constraint fk_external_control_source_id_external_source_id
        foreign key (source_id) references external_source (id)
);

create table control_sources (
    control_id          BIGINT not null,
    external_control_id BIGINT not null,
    constraint fk_control_sources_control_id_control_id
        foreign key (control_id) references control (id),
    constraint fk_control_sources_external_control_id_external_control_id
        foreign key (external_control_id) references external_control (id)
);

