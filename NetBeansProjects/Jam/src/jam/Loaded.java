package jam;

import java.util.List;

public class Loaded {
    public List<Part> registeredParts;
    
    public class Part {
        public int power, weight, fuel;
        public boolean isEngine, isController;
        public String texture, poweredTexture;
    }
}
