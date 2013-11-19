package lunatrius.laserlevel.client;

import lunatrius.laserlevel.TileEntityLaserLevel;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import org.lwjgl.opengl.GL11;

public class TileEntityLaserLevelRenderer extends TileEntitySpecialRenderer {
	public static int cube;

	public TileEntityLaserLevelRenderer() {
		cube = GL11.glGenLists(1);
		recompileList();
	}

	private static void recompileList() {
		float size = 0.5f;
		GL11.glNewList(cube, GL11.GL_COMPILE);
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glVertex3f(+size, -size, -size);
		GL11.glVertex3f(-size, -size, -size);
		GL11.glVertex3f(-size, +size, -size);
		GL11.glVertex3f(+size, +size, -size);
		GL11.glVertex3f(-size, -size, -size);
		GL11.glVertex3f(-size, -size, +size);
		GL11.glVertex3f(-size, +size, +size);
		GL11.glVertex3f(-size, +size, -size);
		GL11.glVertex3f(+size, -size, +size);
		GL11.glVertex3f(+size, -size, -size);
		GL11.glVertex3f(+size, +size, -size);
		GL11.glVertex3f(+size, +size, +size);
		GL11.glVertex3f(-size, -size, -size);
		GL11.glVertex3f(+size, -size, -size);
		GL11.glVertex3f(+size, -size, +size);
		GL11.glVertex3f(-size, -size, +size);
		GL11.glVertex3f(-size, -size, +size);
		GL11.glVertex3f(+size, -size, +size);
		GL11.glVertex3f(+size, +size, +size);
		GL11.glVertex3f(-size, +size, +size);
		GL11.glVertex3f(-size, +size, -size);
		GL11.glVertex3f(-size, +size, +size);
		GL11.glVertex3f(+size, +size, +size);
		GL11.glVertex3f(+size, +size, -size);
		GL11.glEnd();

		GL11.glBegin(GL11.GL_LINE_LOOP);
		GL11.glVertex3f(+size, -size, -size);
		GL11.glVertex3f(-size, -size, -size);
		GL11.glVertex3f(-size, +size, -size);
		GL11.glVertex3f(+size, +size, -size);
		GL11.glEnd();

		GL11.glBegin(GL11.GL_LINE_LOOP);
		GL11.glVertex3f(-size, -size, -size);
		GL11.glVertex3f(-size, -size, +size);
		GL11.glVertex3f(-size, +size, +size);
		GL11.glVertex3f(-size, +size, -size);
		GL11.glEnd();

		GL11.glBegin(GL11.GL_LINE_LOOP);
		GL11.glVertex3f(+size, -size, +size);
		GL11.glVertex3f(+size, -size, -size);
		GL11.glVertex3f(+size, +size, -size);
		GL11.glVertex3f(+size, +size, +size);
		GL11.glEnd();

		GL11.glBegin(GL11.GL_LINE_LOOP);
		GL11.glVertex3f(-size, -size, -size);
		GL11.glVertex3f(+size, -size, -size);
		GL11.glVertex3f(+size, -size, +size);
		GL11.glVertex3f(-size, -size, +size);
		GL11.glEnd();

		GL11.glBegin(GL11.GL_LINE_LOOP);
		GL11.glVertex3f(-size, -size, +size);
		GL11.glVertex3f(+size, -size, +size);
		GL11.glVertex3f(+size, +size, +size);
		GL11.glVertex3f(-size, +size, +size);
		GL11.glEnd();

		GL11.glBegin(GL11.GL_LINE_LOOP);
		GL11.glVertex3f(-size, +size, -size);
		GL11.glVertex3f(-size, +size, +size);
		GL11.glVertex3f(+size, +size, +size);
		GL11.glVertex3f(+size, +size, -size);
		GL11.glEnd();
		GL11.glEndList();
	}

	@Override
	public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float par) {
		for (int i = 0; i < 3; i++) {
			if ((((TileEntityLaserLevel) tileEntity).axis & (1 << i)) != 0) {
				this.renderRuler((TileEntityLaserLevel) tileEntity, x, y, z, i);
			}
		}
	}

	private void renderRuler(TileEntityLaserLevel tileEntityLaserLevel, double x, double y, double z, int direction) {
		GL11.glPushMatrix();
		GL11.glTranslated(x, y, z);
		GL11.glColor4d(1.0, 0.25, 0.25, 1.0);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glLineWidth(1.0f);
		GL11.glTranslatef(0.5f, 0.5f, 0.5f);

		switch (direction) {
		case 0:
			GL11.glRotatef(180.0f, 0.0f, 1.0f, 0.0f);
			break;

		case 1:
			GL11.glRotatef(-90.0f, 0.0f, 0.0f, 1.0f);
			break;

		case 2:
			GL11.glRotatef(90.0f, 0.0f, 1.0f, 0.0f);
			break;
		}

		GL11.glBegin(GL11.GL_LINES);
		GL11.glVertex3d(64.0, 0.0, 0.0);
		GL11.glVertex3d(-64.0, 0.0, 0.0);
		GL11.glEnd();

		GL11.glColor4d(0.25, 1.0, 0.25, 0.5);
		int i;

		for (i = 0; i <= 64 && tileEntityLaserLevel.spacer > 0; i += tileEntityLaserLevel.spacer) {
			renderCube(i + tileEntityLaserLevel.offset % tileEntityLaserLevel.spacer);
		}

		for (i = -tileEntityLaserLevel.spacer; i >= -64 && tileEntityLaserLevel.spacer > 0; i -= tileEntityLaserLevel.spacer) {
			renderCube(i + tileEntityLaserLevel.offset % tileEntityLaserLevel.spacer);
		}

		switch (direction) {
		case 0:
			GL11.glRotatef(-180.0f, 0.0f, 1.0f, 0.0f);
			break;

		case 1:
			GL11.glRotatef(90.0f, 0.0f, 0.0f, 1.0f);
			break;

		case 2:
			GL11.glRotatef(-90.0f, 0.0f, 1.0f, 0.0f);
			break;
		}

		GL11.glDisable(GL11.GL_LINE_SMOOTH);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		GL11.glPopMatrix();
	}

	private static void renderCube(int x) {
		GL11.glTranslatef(-x, 0.0f, 0.0f);
		GL11.glScalef(0.5f, 0.5f, 0.5f);
		GL11.glCallList(cube);
		GL11.glScalef(2.0f, 2.0f, 2.0f);
		GL11.glTranslatef(x, 0.0f, 0.0f);
	}
}
