import org.junit.Assert.*
import org.junit.Test

class VpnViewModelTest {
    @Test
    fun invalidIndexReturnsError() {
        val vm = VpnViewModel()
        val result = kotlinx.coroutines.runBlocking { vm.connect(0, "/tmp/nonexist.ovpn") }
        assertEquals("Invalid host index", result)
    }

    @Test
    fun missingPathReturnsError() {
        val vm = VpnViewModel()
        vm.javaClass.getDeclaredField("hostNames").apply {
            isAccessible = true
            (get(vm) as MutableList<String>).add("test")
        }
        vm.javaClass.getDeclaredField("hostMap").apply {
            isAccessible = true
            (get(vm) as MutableMap<String, String>)["test"] = "1.2.3.4"
        }
        val result = kotlinx.coroutines.runBlocking { vm.connect(0, null) }
        assertEquals("OVPN path required", result)
    }
}
