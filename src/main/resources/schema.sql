create table m_banks
(
    id           int auto_increment
        primary key,
    created_at   datetime(6)  null,
    discarded_at datetime(6)  null,
    updated_at   datetime(6)  null,
    address      varchar(255) not null,
    name         varchar(255) not null
);

create table m_roles
(
    id   varchar(255)                                           not null
        primary key,
    role enum ('ROLE_SUPER_ADMIN', 'ROLE_ADMIN', 'ROLE_MEMBER') null
);

create table m_stocks
(
    lot          int          null,
    price        double       null,
    created_at   datetime(6)  null,
    discarded_at datetime(6)  null,
    updated_at   datetime(6)  null,
    company      varchar(255) null,
    id           varchar(255) not null
        primary key,
    name         varchar(255) null
);

create table m_users
(
    balance         decimal(38, 2) null,
    birth_date      datetime(6)    null,
    created_at      datetime(6)    null,
    discarded_at    datetime(6)    null,
    updated_at      datetime(6)    null,
    address         varchar(255)   null,
    email           varchar(255)   null,
    id              varchar(255)   not null
        primary key,
    name            varchar(255)   null,
    password        varchar(255)   null,
    profile_picture varchar(255)   null,
    username        varchar(255)   null,
        unique (email)
);

create table t_portfolios
(
    avg_buy      decimal(38, 2) null,
    created_at   datetime(6)    null,
    discarded_at datetime(6)    null,
    updated_at   datetime(6)    null,
    id           varchar(255)   not null
        primary key,
    returns      varchar(255)   null,
    user_id      varchar(255)   null,
        unique (user_id),
        foreign key (user_id) references m_users (id)
);

create table t_transactions
(
    bank_id          int                  null,
    lot              int                  null,
    price            double               not null,
    created_at       datetime(6)          null,
    discarded_at     datetime(6)          null,
    updated_at       datetime(6)          null,
    id               varchar(255)         not null
        primary key,
    portfolio_id     varchar(255)         null,
    stock_id         varchar(255)         null,
    transaction_type enum ('BUY', 'SELL') null,
        foreign key (portfolio_id) references t_portfolios (id),
        foreign key (bank_id) references m_banks (id),
        foreign key (stock_id) references m_stocks (id)
);

create table t_user_roles
(
    role_id varchar(255) not null,
    user_id varchar(255) not null,

        foreign key (user_id) references m_users (id),
        foreign key (role_id) references m_roles (id)
);

