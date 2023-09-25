package pl.macia.printinghouse.server.repository

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository
import pl.macia.printinghouse.server.bmodel.Image
import pl.macia.printinghouse.server.bmodel.ImageImpl
import pl.macia.printinghouse.server.dao.ImageDAO
import pl.macia.printinghouse.server.dto.Image as PImage

@Repository
internal class ImageRepoImpl : ImageIntRepo {
    @Autowired
    lateinit var dao: ImageDAO
    private fun PImage.toBiz(): Image {
        return ImageImpl(this)
    }

    override fun findById(id: Long): Image? {
        return dao.findByIdOrNull(id)?.toBiz()
    }

    override fun save(obj: Image): Image {
        obj as ImageImpl
        return dao.save(obj.persistent).toBiz()
    }
}