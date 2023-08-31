package pl.macia.printinghouse.server.dto

import dev.drewhamilton.poko.Poko
import jakarta.persistence.*
import org.hibernate.validator.constraints.URL

const val tableURL = "URL"
const val urlId = "id"
const val urlUrl = "url"

@Poko
@Entity
@Table(name = tableURL)
class URL(
    @Id
    @Column(name = urlId)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long?,
    @Column(name = urlUrl)
    @field:URL
    var url: String
) {
    constructor(url: String) : this(null, url)
}