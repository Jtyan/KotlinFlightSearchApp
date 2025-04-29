package learningprogramming.academy.kotlinflightsearchapp.data.local

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.google.protobuf.InvalidProtocolBufferException
import learningprogramming.academy.kotlinflightsearchapp.SelectedAirport
import java.io.InputStream
import java.io.OutputStream


object SelectedAirportSerializer : Serializer<SelectedAirport> {
    override val defaultValue: SelectedAirport = SelectedAirport.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): SelectedAirport {
        try {
            return SelectedAirport.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto", exception)
        }
    }

    override suspend fun writeTo(t: SelectedAirport, output: OutputStream) {
        t.writeTo(output)
    }
}