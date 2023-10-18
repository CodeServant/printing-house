package pl.macia.printinghouse.request

interface EmployeeChangeReq {
    val nullingRest: Boolean
    val employed: Boolean?
    val activeAccount: Boolean?
    val password: String?
    val email: String?
    val psudoPESEL: String?
    val surname: String?
    val name: String?
}