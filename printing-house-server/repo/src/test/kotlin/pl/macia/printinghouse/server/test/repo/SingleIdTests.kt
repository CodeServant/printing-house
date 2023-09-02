package pl.macia.printinghouse.server.test.repo

import org.junit.jupiter.api.Assertions
import pl.macia.printinghouse.server.repository.BaseRepo
import kotlin.reflect.KProperty

internal class SingleIdTests<T, ID>(private val repo: BaseRepo<T>) {

    fun createNew(obj: T, id: KProperty<ID?>, findBy: (ID) -> T?) {
        repo.save(obj)
        Assertions.assertNotNull(id.call())
        val saved = findBy(id.call()!!)
        Assertions.assertNotNull(saved)
    }
}