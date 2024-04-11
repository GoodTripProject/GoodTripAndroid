package ru.hse.goodtrip.network.trips.model;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.locationtech.jts.geom.impl.CoordinateArraySequence;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CityVisit implements Serializable {

  private Integer id;


  private String city;

  @JsonSerialize(using = PointCustomSerializer.class)
  @JsonDeserialize(using = PointCustomDeserializer.class)
  private Point point;

  private Integer countryVisitId;

  static class PointCustomSerializer extends StdSerializer<Point> {

    protected PointCustomSerializer() {
      super(Point.class, true);
    }

    @Override
    public void serialize(Point point, JsonGenerator jsonGenerator,
        SerializerProvider serializerProvider) throws IOException {
      jsonGenerator.writeStartObject();
      jsonGenerator.writeNumberField("x", point.getX());
      jsonGenerator.writeNumberField("y", point.getY());
      jsonGenerator.writeEndObject();
    }
  }

  static class PointCustomDeserializer extends StdDeserializer<Point> {

    private final static int SRID = 4326;

    protected PointCustomDeserializer() {
      super(Point.class);
    }

    @Override
    public Point deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
        throws IOException {
      JsonNode node = jsonParser.getCodec().readTree(jsonParser);
      Double x = (Double) node.get("x").numberValue();
      Double y = (Double) node.get("y").numberValue();
      return new Point(new CoordinateArraySequence(new Coordinate[]{new Coordinate(x, y)}),
          new GeometryFactory(new PrecisionModel(), SRID));
    }
  }
}
