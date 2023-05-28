package org.example.server.model;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.TextNode;
import lombok.Data;

import java.awt.*;
import java.io.IOException;

/**
 * @version 1.0
 * @author Denis Moskvin
 */
@Data
public class Square {
    private int x;
    private int y;
    private int col;
    private int row;
    @JsonSerialize(using = ColorSerializer.class)
    @JsonDeserialize(using = ColorDeserializer.class)
    private Color color;

    public Square(int x, int y, int col, int row, Color color) {
        this.x = x;
        this.y = y;
        this.col = col;
        this.row = row;
        this.color = color;
    }

    public Square() {

    }

    public static class ColorSerializer extends JsonSerializer<Color> {
        @Override
        public void serialize(Color value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeStartObject();
            gen.writeFieldName("argb");
            gen.writeString(Integer.toHexString(value.getRGB()));
            gen.writeEndObject();
        }
    }

    public static class ColorDeserializer extends StdDeserializer<Color> {
        public ColorDeserializer() {
            super(Color.class);
        }

        @Override
        public Color deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            TreeNode root = p.getCodec().readTree(p);
            TextNode rgba = (TextNode) root.get("argb");
            if (rgba != null) {
                return new Color(Integer.parseUnsignedInt(rgba.textValue(), 16), true);
            } else {
                // Return a default color value if rgba is null
                return Color.BLACK; // Or any other default color of your choice
            }
        }
    }
}
