package com.sanislo.vodapp

import com.sanislo.vodapp.data.entity.*


/**
 * Test data for unit tests.
 */
object TestData {
    val ENTRY_0 = Entry(
        0L,
        listOf(
            Category(
                "d_0", "id_0", "title_0"
            ),
            Category(
                "d_1", "id_1", "title_1"
            )
        ),
        listOf(
            Content(0, "format_0", false, 0, "id_0", "l_0", "url", 0)
        ),
        listOf(),
        "d_0",
        "id_0",
        listOf(
            Image(0, "id", "type", "url_0", 0)
        ),
        listOf(),
        listOf(),
        0L,
        "title_0",
        "type_0"
    )
    val ENTRY_1 = Entry(
        0L,
        listOf(
            Category(
                "d_0", "id_0", "title_0"
            ),
            Category(
                "d_1", "id_1", "title_1"
            )
        ),
        listOf(
            Content(0, "format_0", false, 0, "id_0", "l_0", "url", 0)
        ),
        listOf(),
        "d_1",
        "id_1",
        listOf(
            Image(0, "id", "type", "url_1", 0)
        ),
        listOf(),
        listOf(),
        0L,
        "title_1",
        "type_0"
    )
    val ENTRY_2 = Entry(
        0L,
        listOf(
            Category(
                "d_0", "id_0", "title_0"
            ),
            Category(
                "d_1", "id_1", "title_1"
            )
        ),
        listOf(
            Content(0, "format_0", false, 0, "id_0", "l_0", "url", 0)
        ),
        listOf(),
        "d_2",
        "id_2",
        listOf(
            Image(0, "id", "type", "url_2", 0)
        ),
        listOf(),
        listOf(),
        0L,
        "title_2",
        "type_0"
    )

    val vodsResponse = VodsResponse(
        listOf(
            ENTRY_0,
            ENTRY_1,
            ENTRY_2
        ),
        3
    )
}
