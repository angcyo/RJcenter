class GClass {
    def name
    def age

    String toString() {
        "${name}, ${age}"
    }

    def add(x, y) {
        return x + y
    }
}

class GClass2 {
    def name
    def age

    String toString() {
        "GClass2--> ${name}, ${age}"
    }

    def value(){
        return 200
    }
}