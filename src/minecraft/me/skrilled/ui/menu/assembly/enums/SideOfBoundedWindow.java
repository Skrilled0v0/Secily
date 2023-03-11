package me.skrilled.ui.menu.assembly.enums;

public enum SideOfBoundedWindow {
    LEFT {
        @Override
        public float[] calcDeltaPos(int deltaMouseX, int deltaMouseY, boolean shift, float[] boundedWindowDeltaPos) {
            float[] floats;
            floats = new float[]{deltaMouseX, shift ? deltaMouseX * boundedWindowDeltaPos[1] / boundedWindowDeltaPos[0] : 0, 0, 0};
            return floats;
        }
    }, DOWN {
        @Override
        public float[] calcDeltaPos(int deltaMouseX, int deltaMouseY, boolean shift, float[] boundedWindowDeltaPos) {
            return new float[]{0, 0, shift ? deltaMouseY * boundedWindowDeltaPos[0] / boundedWindowDeltaPos[1] : 0, deltaMouseY};
        }
    }, RIGHT {
        @Override
        public float[] calcDeltaPos(int deltaMouseX, int deltaMouseY, boolean shift, float[] boundedWindowDeltaPos) {
            return new float[]{0, 0, deltaMouseX, shift ? deltaMouseX * boundedWindowDeltaPos[1] / boundedWindowDeltaPos[0] : 0};
        }
    }, UP {
        @Override
        public float[] calcDeltaPos(int deltaMouseX, int deltaMouseY, boolean shift, float[] boundedWindowDeltaPos) {
            return new float[]{shift ? deltaMouseY * boundedWindowDeltaPos[0] / boundedWindowDeltaPos[1] : 0, deltaMouseY, 0, 0};
        }
    }, NULL {
        @Override
        public float[] calcDeltaPos(int deltaMouseX, int deltaMouseY, boolean shift, float[] boundedWindowDeltaPos) {
            return new float[4];
        }
    };

    public abstract float[] calcDeltaPos(int deltaMouseX, int deltaMouseY, boolean shift, float[] boundedWindowDeltaPos);
}
