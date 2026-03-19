package com.byteblox;

public class Block {
    private BlockType type;

    public Block(BlockType type) {
        this.type = type;
    }

    public BlockType getType() {
        return type;
    }

    public void setType(BlockType type) {
        this.type = type;
    }

    public String getSymbol() {
        return type.getSymbol();
    }


    @Override
    public String toString() {
        return getSymbol();
    }

    public enum BlockType {
        BORDER("."),
       
        DIAMOND("<>"); // <-- Added this

        private final String symbol;

        BlockType(String symbol) {
            this.symbol = symbol;
        }

        public String getSymbol() {
            return symbol;
        }
    }

}
