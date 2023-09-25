package pl.macia.printinghouse.server.dto

import dev.drewhamilton.poko.Poko
import jakarta.persistence.*
import jakarta.validation.constraints.Size
import org.hibernate.validator.constraints.URL as AURL

@Poko
@Entity
@Table(name = Image.TABLE_NAME)
internal class Image(
    @Id
    @Column(name = ID)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long?,
    @Column(name = URL)
    @field:AURL
    var url: String,
    @Column(name = COMMENT, nullable = true)
    @field:Size(min = 1)
    var imageComment: String?,
) {
    constructor(url: String, comment: String?) : this(null, url, comment)

    companion object {
        const val TABLE_NAME = "Image"
        const val ID = "id"
        const val URL = "url"
        const val COMMENT = "imageComment"
    }
}