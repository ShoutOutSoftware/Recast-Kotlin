import com.shoutoutsoftware.recast.Recast
import com.shoutoutsoftware.recast.callback.RecastCallback
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class RecastTransformOnlySelectedKeysTest {

    @Test
    fun testTransformationOfaComplexHashMap() {
        val map = java.util.HashMap<String, Any?>()
        map["string"] = "value"
        map["int"] = 1
        map["float"] = 1.2.toFloat()
        map["double"] = 1.2
        map["map"] = hashMapOf("subStr" to "x", "subInt" to 1)
        var numberOfCallbacks = 0

        val keysToAlter = listOf("string", "float", "subInt")

        Recast().transformByChangingValueTypes(map, keysToAlter, callback = RecastCallback { _, key ->
            assertTrue(keysToAlter.contains(key))
            numberOfCallbacks++
        })
        assertEquals(numberOfCallbacks, 3)
    }

}