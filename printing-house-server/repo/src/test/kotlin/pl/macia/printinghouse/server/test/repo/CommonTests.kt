package pl.macia.printinghouse.server.test.repo

import org.junit.jupiter.api.Assertions
import pl.macia.printinghouse.server.repository.BaseRepo
import kotlin.reflect.KProperty

internal class CommonTests<T, ID>(val repo: BaseRepo<T, ID>) {

    fun createNew(obj: T, id: KProperty<ID?>){
        var new = obj
        repo.save(new)
        Assertions.assertNotNull(id.call())
        val saved = repo.findById(id.call()!!)
        Assertions.assertNotNull(saved)
    }
}