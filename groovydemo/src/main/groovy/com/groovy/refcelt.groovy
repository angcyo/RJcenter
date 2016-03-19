

def binning(object, maps, targets){
    if (maps.size() < 1 || targets.size() < 1) {
        return;
    }
    def json = maps.remove(0);
    for (int i = 0; i < targets.size(); i++) {
        def field = targets.get(i);
        if (json.key.equals(field.getName())) {
            field.set(object, json.vaule);
            targets.remove(i);
            break;
        }
    }
    binning(object, maps, targets);
}

class Rsen{
    def r_name
    def r_age
}

rsen = new Rsen()
jons = new ArrayList<>()
jons.add(new HashMap<>())

binding(rsen,  )