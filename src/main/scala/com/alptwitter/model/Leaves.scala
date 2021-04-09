package com.alptwitter.model

case class Leaves(
    is_archived: BigDecimal,
    all: List[String],
    is_starred: BigDecimal,
    user_name: String,
    user_email: String,
    user_id: BigDecimal,
    tags: List[String],
    slugs: List[String],
    is_public: Boolean,
    id: String,
    title: String,
    url: String,
    content_text: String,
    created_at: String,
    updated_at: String,
    mimetype: String,
    language: String,
    reading_time: BigDecimal,
    domain_name: String,
    preview_picture: String,
    contact: Leaves2
)

case class Leaves2(
    http_status: String, 
    links: List[String], 
    content: String
)

case class MyUrl(
    url: String
)
