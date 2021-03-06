package logisticspipes.renderer.newpipe;

import java.util.*;

import logisticspipes.interfaces.ITubeOrientation;

import net.minecraft.util.EnumFacing;

public class LogisticsNewRenderPipe implements IHighlightPlacementRenderer {

	@Override
	public void renderHighlight(ITubeOrientation orientation) {

	}

	enum Edge {
		Upper_North(EnumFacing.UP, EnumFacing.NORTH),
		Upper_South(EnumFacing.UP, EnumFacing.SOUTH),
		Upper_East(EnumFacing.UP, EnumFacing.EAST),
		Upper_West(EnumFacing.UP, EnumFacing.WEST),
		Lower_North(EnumFacing.DOWN, EnumFacing.NORTH),
		Lower_South(EnumFacing.DOWN, EnumFacing.SOUTH),
		Lower_East(EnumFacing.DOWN, EnumFacing.EAST),
		Lower_West(EnumFacing.DOWN, EnumFacing.WEST),
		Middle_North_West(EnumFacing.NORTH, EnumFacing.WEST),
		Middle_North_East(EnumFacing.NORTH, EnumFacing.EAST),
		Lower_South_East(EnumFacing.SOUTH, EnumFacing.EAST),
		Lower_South_West(EnumFacing.SOUTH, EnumFacing.WEST);

		final EnumFacing part1;
		final EnumFacing part2;

		Edge(EnumFacing part1, EnumFacing part2) {
			this.part1 = part1;
			this.part2 = part2;
		}
	}

	enum UpDown {
		UP("U", EnumFacing.UP),
		DOWN("D", EnumFacing.DOWN);

		final String s;
		final EnumFacing dir;

		UpDown(String s, EnumFacing dir) {
			this.s = s;
			this.dir = dir;
		}
	}

	enum NorthSouth {
		NORTH("N", EnumFacing.NORTH),
		SOUTH("S", EnumFacing.SOUTH);

		final String s;
		final EnumFacing dir;

		NorthSouth(String s, EnumFacing dir) {
			this.s = s;
			this.dir = dir;
		}
	}

	enum EastWest {
		EAST("E", EnumFacing.EAST),
		WEST("W", EnumFacing.WEST);

		final String s;
		final EnumFacing dir;

		EastWest(String s, EnumFacing dir) {
			this.s = s;
			this.dir = dir;
		}
	}

	enum Corner {
		UP_NORTH_WEST(UpDown.UP, NorthSouth.NORTH, EastWest.WEST),
		UP_NORTH_EAST(UpDown.UP, NorthSouth.NORTH, EastWest.EAST),
		UP_SOUTH_WEST(UpDown.UP, NorthSouth.SOUTH, EastWest.WEST),
		UP_SOUTH_EAST(UpDown.UP, NorthSouth.SOUTH, EastWest.EAST),
		DOWN_NORTH_WEST(UpDown.DOWN, NorthSouth.NORTH, EastWest.WEST),
		DOWN_NORTH_EAST(UpDown.DOWN, NorthSouth.NORTH, EastWest.EAST),
		DOWN_SOUTH_WEST(UpDown.DOWN, NorthSouth.SOUTH, EastWest.WEST),
		DOWN_SOUTH_EAST(UpDown.DOWN, NorthSouth.SOUTH, EastWest.EAST);

		final UpDown ud;
		final NorthSouth ns;
		final EastWest ew;

		Corner(UpDown ud, NorthSouth ns, EastWest ew) {
			this.ud = ud;
			this.ns = ns;
			this.ew = ew;
		}
	}

	enum Turn {
		NORTH_SOUTH(EnumFacing.NORTH, EnumFacing.SOUTH),
		EAST_WEST(EnumFacing.EAST, EnumFacing.WEST),
		UP_DOWN(EnumFacing.UP, EnumFacing.DOWN);

		final EnumFacing dir1;
		final EnumFacing dir2;

		Turn(EnumFacing dir1, EnumFacing dir2) {
			this.dir1 = dir1;
			this.dir2 = dir2;
		}
	}

	enum PipeTurnCorner {
		UP_NORTH_WEST_TURN_NORTH_SOUTH(Corner.UP_NORTH_WEST, Turn.NORTH_SOUTH, 1),
		UP_NORTH_WEST_TURN_EAST_WEST(Corner.UP_NORTH_WEST, Turn.EAST_WEST, 14),
		UP_NORTH_WEST_TURN_UP_DOWN(Corner.UP_NORTH_WEST, Turn.UP_DOWN, 23),
		UP_NORTH_EAST_TURN_NORTH_SOUTH(Corner.UP_NORTH_EAST, Turn.NORTH_SOUTH, 2),
		UP_NORTH_EAST_TURN_EAST_WEST(Corner.UP_NORTH_EAST, Turn.EAST_WEST, 9),
		UP_NORTH_EAST_TURN_UP_DOWN(Corner.UP_NORTH_EAST, Turn.UP_DOWN, 22),
		UP_SOUTH_WEST_TURN_NORTH_SOUTH(Corner.UP_SOUTH_WEST, Turn.NORTH_SOUTH, 6),
		UP_SOUTH_WEST_TURN_EAST_WEST(Corner.UP_SOUTH_WEST, Turn.EAST_WEST, 13),
		UP_SOUTH_WEST_TURN_UP_DOWN(Corner.UP_SOUTH_WEST, Turn.UP_DOWN, 24),
		UP_SOUTH_EAST_TURN_NORTH_SOUTH(Corner.UP_SOUTH_EAST, Turn.NORTH_SOUTH, 5),
		UP_SOUTH_EAST_TURN_EAST_WEST(Corner.UP_SOUTH_EAST, Turn.EAST_WEST, 10),
		UP_SOUTH_EAST_TURN_UP_DOWN(Corner.UP_SOUTH_EAST, Turn.UP_DOWN, 21),
		DOWN_NORTH_WEST_TURN_NORTH_SOUTH(Corner.DOWN_NORTH_WEST, Turn.NORTH_SOUTH, 4),
		DOWN_NORTH_WEST_TURN_EAST_WEST(Corner.DOWN_NORTH_WEST, Turn.EAST_WEST, 15),
		DOWN_NORTH_WEST_TURN_UP_DOWN(Corner.DOWN_NORTH_WEST, Turn.UP_DOWN, 20),
		DOWN_NORTH_EAST_TURN_NORTH_SOUTH(Corner.DOWN_NORTH_EAST, Turn.NORTH_SOUTH, 3),
		DOWN_NORTH_EAST_TURN_EAST_WEST(Corner.DOWN_NORTH_EAST, Turn.EAST_WEST, 12),
		DOWN_NORTH_EAST_TURN_UP_DOWN(Corner.DOWN_NORTH_EAST, Turn.UP_DOWN, 17),
		DOWN_SOUTH_WEST_TURN_NORTH_SOUTH(Corner.DOWN_SOUTH_WEST, Turn.NORTH_SOUTH, 7),
		DOWN_SOUTH_WEST_TURN_EAST_WEST(Corner.DOWN_SOUTH_WEST, Turn.EAST_WEST, 16),
		DOWN_SOUTH_WEST_TURN_UP_DOWN(Corner.DOWN_SOUTH_WEST, Turn.UP_DOWN, 19),
		DOWN_SOUTH_EAST_TURN_NORTH_SOUTH(Corner.DOWN_SOUTH_EAST, Turn.NORTH_SOUTH, 8),
		DOWN_SOUTH_EAST_TURN_EAST_WEST(Corner.DOWN_SOUTH_EAST, Turn.EAST_WEST, 11),
		DOWN_SOUTH_EAST_TURN_UP_DOWN(Corner.DOWN_SOUTH_EAST, Turn.UP_DOWN, 18);

		final Corner corner;
		final Turn turn;
		final int number;

		PipeTurnCorner(Corner corner, Turn turn, int number) {
			this.corner = corner;
			this.turn = turn;
			this.number = number;
		}

		public EnumFacing getPointer() {
			List<EnumFacing> canidates = new ArrayList<>();
			canidates.add(corner.ew.dir);
			canidates.add(corner.ns.dir);
			canidates.add(corner.ud.dir);
			if (canidates.contains(turn.dir1)) {
				return turn.dir1;
			} else if (canidates.contains(turn.dir2)) {
				return turn.dir2;
			} else {
				throw new UnsupportedOperationException(name());
			}
		}
	}

	enum PipeSupportOri {
		UP_DOWN("U"),
		SIDE("S");

		final String s;

		PipeSupportOri(String s) {
			this.s = s;
		}
	}

	enum PipeSupport {
		UP_UP(EnumFacing.UP, PipeSupportOri.UP_DOWN),
		UP_SIDE(EnumFacing.UP, PipeSupportOri.SIDE),
		DOWN_UP(EnumFacing.DOWN, PipeSupportOri.UP_DOWN),
		DOWN_SIDE(EnumFacing.DOWN, PipeSupportOri.SIDE),
		NORTH_UP(EnumFacing.NORTH, PipeSupportOri.UP_DOWN),
		NORTH_SIDE(EnumFacing.NORTH, PipeSupportOri.SIDE),
		SOUTH_UP(EnumFacing.SOUTH, PipeSupportOri.UP_DOWN),
		SOUTH_SIDE(EnumFacing.SOUTH, PipeSupportOri.SIDE),
		EAST_UP(EnumFacing.EAST, PipeSupportOri.UP_DOWN),
		EAST_SIDE(EnumFacing.EAST, PipeSupportOri.SIDE),
		WEST_UP(EnumFacing.WEST, PipeSupportOri.UP_DOWN),
		WEST_SIDE(EnumFacing.WEST, PipeSupportOri.SIDE);

		PipeSupport(EnumFacing dir, PipeSupportOri ori) {
			this.dir = dir;
			this.ori = ori;
		}

		final EnumFacing dir;
		final PipeSupportOri ori;
	}

	enum PipeMount {
		UP_NORTH(EnumFacing.UP, EnumFacing.NORTH),
		UP_SOUTH(EnumFacing.UP, EnumFacing.SOUTH),
		UP_EAST(EnumFacing.UP, EnumFacing.EAST),
		UP_WEST(EnumFacing.UP, EnumFacing.WEST),
		DOWN_NORTH(EnumFacing.DOWN, EnumFacing.NORTH),
		DOWN_SOUTH(EnumFacing.DOWN, EnumFacing.SOUTH),
		DOWN_EAST(EnumFacing.DOWN, EnumFacing.EAST),
		DOWN_WEST(EnumFacing.DOWN, EnumFacing.WEST),
		NORTH_UP(EnumFacing.NORTH, EnumFacing.UP),
		NORTH_DOWN(EnumFacing.NORTH, EnumFacing.DOWN),
		NORTH_EAST(EnumFacing.NORTH, EnumFacing.EAST),
		NORTH_WEST(EnumFacing.NORTH, EnumFacing.WEST),
		SOUTH_UP(EnumFacing.SOUTH, EnumFacing.UP),
		SOUTH_DOWN(EnumFacing.SOUTH, EnumFacing.DOWN),
		SOUTH_EAST(EnumFacing.SOUTH, EnumFacing.EAST),
		SOUTH_WEST(EnumFacing.SOUTH, EnumFacing.WEST),
		EAST_UP(EnumFacing.EAST, EnumFacing.UP),
		EAST_DOWN(EnumFacing.EAST, EnumFacing.DOWN),
		EAST_NORTH(EnumFacing.EAST, EnumFacing.NORTH),
		EAST_SOUTH(EnumFacing.EAST, EnumFacing.SOUTH),
		WEST_UP(EnumFacing.WEST, EnumFacing.UP),
		WEST_DOWN(EnumFacing.WEST, EnumFacing.DOWN),
		WEST_NORTH(EnumFacing.WEST, EnumFacing.NORTH),
		WEST_SOUTH(EnumFacing.WEST, EnumFacing.SOUTH);

		EnumFacing dir;
		EnumFacing side;

		PipeMount(EnumFacing dir, EnumFacing side) {
			this.dir = dir;
			this.side = side;
		}
	}
/*
	static Map<EnumFacing, List<IModel3D>> sideNormal = new HashMap<>();
	static Map<EnumFacing, List<IModel3D>> sideBC = new HashMap<>();
	static Map<Edge, IModel3D> edges = new HashMap<>();
	static Map<Corner, List<IModel3D>> corners_M = new HashMap<>();
	static Map<Corner, List<IModel3D>> corners_I3 = new HashMap<>();
	static Map<PipeTurnCorner, IModel3D> corners_I = new HashMap<>();
	static Map<PipeSupport, IModel3D> supports = new HashMap<>();
	static Map<PipeTurnCorner, IModel3D> spacers = new HashMap<>();
	static Map<PipeMount, IModel3D> mounts = new HashMap<>();

	static Map<EnumFacing, List<IModel3D>> texturePlate_Inner = new HashMap<>();
	static Map<EnumFacing, List<IModel3D>> texturePlate_Outer = new HashMap<>();
	static Map<EnumFacing, Quartet<List<IModel3D>, List<IModel3D>, List<IModel3D>, List<IModel3D>>> sideTexturePlate = new HashMap<>();
	static Map<PipeMount, List<IModel3D>> textureConnectorPlate = new HashMap<>();

	static Map<ScaleObject, IModel3D> scaleMap = new HashMap<>();

	@Data
	@AllArgsConstructor
	private static class ScaleObject {

		private final IModel3D original;
		private final double scale;
	}

	static IModel3D innerTransportBox;
	static IModel3D highlight;

	public static IIconTransformation basicPipeTexture;
	public static IIconTransformation inactiveTexture;
	public static IIconTransformation glassCenterTexture;
	public static IIconTransformation innerBoxTexture;
	public static IIconTransformation statusTexture;
	public static IIconTransformation statusBCTexture;

	static {
		LogisticsNewRenderPipe.loadModels();
	}

	public static void loadModels() {
		if (!SimpleServiceLocator.cclProxy.isActivated()) return;
		try {
			Map<String, IModel3D> pipePartModels = SimpleServiceLocator.cclProxy.parseObjModels(LogisticsPipes.class.getResourceAsStream("/logisticspipes/models/PipeModel_moved.obj"), 7, new LPScale(1 / 100f));
			List<IModel3D> highlightList = new ArrayList<>();

			for (EnumFacing dir : EnumFacing.VALUES) {
				LogisticsNewRenderPipe.sideNormal.put(dir, new ArrayList<>());
				String grp = "Side_" + LogisticsNewRenderPipe.getDirAsString_Type1(dir);
				pipePartModels.entrySet().stream()
						.filter(entry -> entry.getKey().contains(" " + grp + " ") || entry.getKey().endsWith(" " + grp))
						.forEach(entry -> LogisticsNewRenderPipe.sideNormal.get(dir).add(LogisticsNewRenderPipe.compute(entry.getValue().backfacedCopy().apply(new LPTranslation(0.0, 0.0, 1.0)))));
				if (LogisticsNewRenderPipe.sideNormal.get(dir).size() != 4) {
					throw new RuntimeException("Couldn't load " + dir.name() + " (" + grp + "). Only loaded " + LogisticsNewRenderPipe.sideNormal.get(dir).size());
				}
			}

			for (EnumFacing dir : EnumFacing.VALUES) {
				LogisticsNewRenderPipe.sideBC.put(dir, new ArrayList<>());
				String grp = "Side_BC_" + LogisticsNewRenderPipe.getDirAsString_Type1(dir);
				pipePartModels.entrySet().stream()
						.filter(entry -> entry.getKey().contains(" " + grp + " ") || entry.getKey().endsWith(" " + grp))
						.forEach(entry -> LogisticsNewRenderPipe.sideBC.get(dir).add(LogisticsNewRenderPipe.compute(entry.getValue().backfacedCopy().apply(new LPTranslation(0.0, 0.0, 1.0)))));
				if (LogisticsNewRenderPipe.sideBC.get(dir).size() != 8) {
					throw new RuntimeException("Couldn't load " + dir.name() + " (" + grp + "). Only loaded " + LogisticsNewRenderPipe.sideBC.get(dir).size());
				}
			}

			for (Edge edge : Edge.values()) {
				String grp;
				if (edge.part1 == EnumFacing.UP || edge.part1 == EnumFacing.DOWN) {
					grp = "Edge_M_" + LogisticsNewRenderPipe.getDirAsString_Type1(edge.part1) + "_" + LogisticsNewRenderPipe.getDirAsString_Type1(edge.part2);
				} else {
					grp = "Edge_M_S_" + LogisticsNewRenderPipe.getDirAsString_Type1(edge.part1) + LogisticsNewRenderPipe.getDirAsString_Type1(edge.part2);
				}
				for (Entry<String, IModel3D> entry : pipePartModels.entrySet()) {
					if (entry.getKey().contains(" " + grp + " ") || entry.getKey().endsWith(" " + grp)) {
						LogisticsNewRenderPipe.edges.put(edge, LogisticsNewRenderPipe.compute(entry.getValue().backfacedCopy().apply(new LPTranslation(0.0, 0.0, 1.0))));
						break;
					}
				}
				if (LogisticsNewRenderPipe.edges.get(edge) == null) {
					throw new RuntimeException("Couldn't load " + edge.name() + " (" + grp + ")");
				}
				highlightList.add(LogisticsNewRenderPipe.edges.get(edge));
			}

			for (Corner corner : Corner.values()) {
				LogisticsNewRenderPipe.corners_M.put(corner, new ArrayList<>());
				String grp = "Corner_M_" + corner.ud.s + "_" + corner.ns.s + corner.ew.s;
				pipePartModels.entrySet().stream()
						.filter(entry -> entry.getKey().contains(" " + grp + " ") || entry.getKey().endsWith(" " + grp))
						.forEach(entry -> LogisticsNewRenderPipe.corners_M.get(corner).add(LogisticsNewRenderPipe.compute(entry.getValue().backfacedCopy().apply(new LPTranslation(0.0, 0.0, 1.0)))));
				if (LogisticsNewRenderPipe.corners_M.get(corner).size() != 2) {
					throw new RuntimeException("Couldn't load " + corner.name() + " (" + grp + "). Only loaded " + LogisticsNewRenderPipe.corners_M.get(corner).size());
				}
				highlightList.addAll(LogisticsNewRenderPipe.corners_M.get(corner));
			}

			for (Corner corner : Corner.values()) {
				LogisticsNewRenderPipe.corners_I3.put(corner, new ArrayList<>());
				String grp = "Corner_I3_" + corner.ud.s + "_" + corner.ns.s + corner.ew.s;
				pipePartModels.entrySet().stream()
						.filter(entry -> entry.getKey().contains(" " + grp + " ") || entry.getKey().endsWith(" " + grp))
						.forEach(entry -> LogisticsNewRenderPipe.corners_I3.get(corner).add(LogisticsNewRenderPipe.compute(entry.getValue().backfacedCopy().apply(new LPTranslation(0.0, 0.0, 1.0)))));
				if (LogisticsNewRenderPipe.corners_I3.get(corner).size() != 2) {
					throw new RuntimeException("Couldn't load " + corner.name() + " (" + grp + "). Only loaded " + LogisticsNewRenderPipe.corners_I3.get(corner).size());
				}
			}

			for (PipeSupport support : PipeSupport.values()) {
				String grp = "Support_" + LogisticsNewRenderPipe.getDirAsString_Type1(support.dir) + "_" + support.ori.s;
				for (Entry<String, IModel3D> entry : pipePartModels.entrySet()) {
					if (entry.getKey().contains(" " + grp + " ") || entry.getKey().endsWith(" " + grp)) {
						LogisticsNewRenderPipe.supports.put(support, LogisticsNewRenderPipe.compute(entry.getValue().backfacedCopy().apply(new LPTranslation(0.0, 0.0, 1.0))));
						break;
					}
				}
				if (LogisticsNewRenderPipe.supports.get(support) == null) {
					throw new RuntimeException("Couldn't load " + support.name() + " (" + grp + ")");
				}
			}

			for (PipeTurnCorner corner : PipeTurnCorner.values()) {
				String grp = "Corner_I_" + corner.corner.ud.s + "_" + corner.corner.ns.s + corner.corner.ew.s;
				for (Entry<String, IModel3D> entry : pipePartModels.entrySet()) {
					if (entry.getKey().contains(" " + grp)) {
						char c = ' ';
						if (!entry.getKey().endsWith(" " + grp)) {
							c = entry.getKey().charAt(entry.getKey().indexOf(" " + grp) + (" " + grp).length());
						}
						if (Character.isDigit(c)) {
							if (c == '2') {
								if (corner.turn != Turn.NORTH_SOUTH) {
									continue;
								}
							} else if (c == '1') {
								if (corner.turn != Turn.EAST_WEST) {
									continue;
								}
							} else {
								throw new UnsupportedOperationException();
							}
						} else {
							if (corner.turn != Turn.UP_DOWN) {
								continue;
							}
						}
						LogisticsNewRenderPipe.corners_I.put(corner, LogisticsNewRenderPipe.compute(entry.getValue().backfacedCopy().apply(new LPTranslation(0.0, 0.0, 1.0))));
						break;
					}
				}
				if (LogisticsNewRenderPipe.corners_I.get(corner) == null) {
					throw new RuntimeException("Couldn't load " + corner.name() + " (" + grp + ")");
				}
			}

			for (PipeTurnCorner corner : PipeTurnCorner.values()) {
				String grp = "Spacer" + corner.number;
				for (Entry<String, IModel3D> entry : pipePartModels.entrySet()) {
					if (entry.getKey().contains(" " + grp + " ") || entry.getKey().endsWith(" " + grp)) {
						LogisticsNewRenderPipe.spacers.put(corner, LogisticsNewRenderPipe.compute(entry.getValue().backfacedCopy().apply(new LPTranslation(0.0, 0.0, 1.0))));
						break;
					}
				}
				if (LogisticsNewRenderPipe.spacers.get(corner) == null) {
					throw new RuntimeException("Couldn't load " + corner.name() + " (" + grp + ")");
				}
			}

			for (PipeMount mount : PipeMount.values()) {
				String grp = "Mount_" + LogisticsNewRenderPipe.getDirAsString_Type1(mount.dir) + "_" + LogisticsNewRenderPipe.getDirAsString_Type1(mount.side);
				for (Entry<String, IModel3D> entry : pipePartModels.entrySet()) {
					if (entry.getKey().contains(" " + grp + " ") || entry.getKey().endsWith(" " + grp)) {
						LogisticsNewRenderPipe.mounts.put(mount, LogisticsNewRenderPipe.compute(entry.getValue().backfacedCopy().apply(new LPTranslation(0.0, 0.0, 1.0))));
						break;
					}
				}
				if (LogisticsNewRenderPipe.mounts.get(mount) == null) {
					throw new RuntimeException("Couldn't load " + mount.name() + " (" + grp + ")");
				}
			}

			for (EnumFacing dir : EnumFacing.VALUES) {
				LogisticsNewRenderPipe.texturePlate_Inner.put(dir, new ArrayList<>());
				String grp = "Inner_Plate_" + LogisticsNewRenderPipe.getDirAsString_Type1(dir);
				pipePartModels.entrySet().stream().filter(entry -> entry.getKey().contains(" " + grp))
						.forEach(entry -> LogisticsNewRenderPipe.texturePlate_Inner.get(dir).add(LogisticsNewRenderPipe.compute(entry.getValue().backfacedCopy().apply(new LPTranslation(0.0, 0.0, 1.0)))));
				if (LogisticsNewRenderPipe.texturePlate_Inner.get(dir).size() != 2) {
					throw new RuntimeException("Couldn't load " + dir.name() + " (" + grp + "). Only loaded " + LogisticsNewRenderPipe.texturePlate_Inner.get(dir).size());
				}
			}

			for (EnumFacing dir : EnumFacing.VALUES) {
				LogisticsNewRenderPipe.texturePlate_Outer.put(dir, new ArrayList<>());
				String grp = "Texture_Plate_" + LogisticsNewRenderPipe.getDirAsString_Type1(dir);
				pipePartModels.entrySet().stream().filter(entry -> entry.getKey().contains(" " + grp))
						.forEach(entry -> LogisticsNewRenderPipe.texturePlate_Outer.get(dir).add(LogisticsNewRenderPipe.compute(entry.getValue().backfacedCopy().apply(new LPTranslation(0.0, 0.0, 1.0)).apply(new LPTranslation(-0.5, -0.5, -0.5)).apply(new LPScale(1.001D)).apply(new LPTranslation(0.5, 0.5, 0.5)))));
				if (LogisticsNewRenderPipe.texturePlate_Outer.get(dir).size() != 2) {
					throw new RuntimeException("Couldn't load " + dir.name() + " (" + grp + "). Only loaded " + LogisticsNewRenderPipe.texturePlate_Outer.get(dir).size());
				}
			}

			for (EnumFacing dir : EnumFacing.VALUES) {
				LogisticsNewRenderPipe.sideTexturePlate.put(dir, new Quartet<>(new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>()));
				String grp = "Texture_Side_" + LogisticsNewRenderPipe.getDirAsString_Type1(dir);
				for (Entry<String, IModel3D> entry : pipePartModels.entrySet()) {
					if (entry.getKey().contains(" " + grp)) {
						IModel3D model = LogisticsNewRenderPipe.compute(entry.getValue().backfacedCopy().apply(new LPTranslation(0.0, 0.0, 1.0)));
						double sizeA = (model.bounds().max().x() - model.bounds().min().x()) + (model.bounds().max().y() - model.bounds().min().y()) + (model.bounds().max().z() - model.bounds().min().z());
						double dis = Math.pow(model.bounds().min().x() - 0.5D, 2) + Math.pow(model.bounds().min().y() - 0.5D, 2) + Math.pow(model.bounds().min().z() - 0.5D, 2);
						if (sizeA < 0.5D) {
							if ((dis > 0.22 && dis < 0.24) || (dis > 0.38 && dis < 0.40)) {
								LogisticsNewRenderPipe.sideTexturePlate.get(dir).getValue4().add(model);
							} else if ((dis < 0.2 && dis > 0.18) || (dis < 0.36 && dis > 0.34)) {
								LogisticsNewRenderPipe.sideTexturePlate.get(dir).getValue2().add(model);
							} else {
								throw new UnsupportedOperationException("Dis: " + dis);
							}
						} else {
							if ((dis > 0.22 && dis < 0.24) || (dis > 0.38 && dis < 0.40)) {
								LogisticsNewRenderPipe.sideTexturePlate.get(dir).getValue3().add(model);
							} else if ((dis < 0.2 && dis > 0.18) || (dis < 0.36 && dis > 0.34)) {
								LogisticsNewRenderPipe.sideTexturePlate.get(dir).getValue1().add(model);
							} else {
								throw new UnsupportedOperationException("Dis: " + dis);
							}
						}
					}
				}
				if (LogisticsNewRenderPipe.sideTexturePlate.get(dir).getValue1().size() != 8) {
					throw new RuntimeException("Couldn't load " + dir.name() + " (" + grp + "). Only loaded " + LogisticsNewRenderPipe.sideTexturePlate.get(dir).getValue1().size());
				}
				if (LogisticsNewRenderPipe.sideTexturePlate.get(dir).getValue2().size() != 8) {
					throw new RuntimeException("Couldn't load " + dir.name() + " (" + grp + "). Only loaded " + LogisticsNewRenderPipe.sideTexturePlate.get(dir).getValue2().size());
				}
				if (LogisticsNewRenderPipe.sideTexturePlate.get(dir).getValue3().size() != 8) {
					throw new RuntimeException("Couldn't load " + dir.name() + " (" + grp + "). Only loaded " + LogisticsNewRenderPipe.sideTexturePlate.get(dir).getValue3().size());
				}
				if (LogisticsNewRenderPipe.sideTexturePlate.get(dir).getValue4().size() != 8) {
					throw new RuntimeException("Couldn't load " + dir.name() + " (" + grp + "). Only loaded " + LogisticsNewRenderPipe.sideTexturePlate.get(dir).getValue4().size());
				}
			}

			for (PipeMount mount : PipeMount.values()) {
				LogisticsNewRenderPipe.textureConnectorPlate.put(mount, new ArrayList<>());
				String grp = "Texture_Connector_" + LogisticsNewRenderPipe.getDirAsString_Type1(mount.dir) + "_" + LogisticsNewRenderPipe.getDirAsString_Type1(mount.side);
				pipePartModels.entrySet().stream()
						.filter(entry -> entry.getKey().contains(" " + grp + " ") || entry.getKey().endsWith(" " + grp))
						.forEach(entry -> LogisticsNewRenderPipe.textureConnectorPlate.get(mount).add(LogisticsNewRenderPipe.compute(entry.getValue().backfacedCopy().apply(new LPTranslation(0.0, 0.0, 1.0)))));
				if (LogisticsNewRenderPipe.textureConnectorPlate.get(mount).size() != 4) {
					throw new RuntimeException("Couldn't load " + mount.name() + " (" + grp + "). Only loaded " + LogisticsNewRenderPipe.textureConnectorPlate.get(mount).size());
				}
			}

			highlight = SimpleServiceLocator.cclProxy.combine(highlightList);

			pipePartModels = SimpleServiceLocator.cclProxy.parseObjModels(LogisticsPipes.class.getResourceAsStream("/logisticspipes/models/PipeModel_Transport_Box.obj"), 7, new LPScale(1 / 100f));

			LogisticsNewRenderPipe.innerTransportBox = LogisticsNewRenderPipe.compute(pipePartModels.get("InnerTransportBox").backfacedCopy().apply(new LPTranslation(0.0, 0.0, 1.0)).apply(new LPTranslation(-0.5, -0.5, -0.5)).apply(new LPScale(0.99D)).apply(new LPTranslation(0.5, 0.5, 0.5)));

		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static String getDirAsString_Type1(EnumFacing dir) {
		switch (dir) {
			case NORTH:
				return "N";
			case SOUTH:
				return "S";
			case EAST:
				return "E";
			case WEST:
				return "W";
			case UP:
				return "U";
			case DOWN:
				return "D";
			default:
				return "UNKNWON";
		}
	}

	public static IModel3D compute(IModel3D m) {
		m.computeNormals();
		//m.computeLighting(LightModel.standardLightModel);
		m.computeStandardLighting();
		return m;
	}

	public static void registerTextures(IIconRegister iconRegister) {
		if (LogisticsNewRenderPipe.basicPipeTexture == null) {
			LogisticsNewRenderPipe.basicPipeTexture = SimpleServiceLocator.cclProxy.createIconTransformer(iconRegister.registerIcon("logisticspipes:" + "pipes/PipeModel"));
			LogisticsNewRenderPipe.inactiveTexture = SimpleServiceLocator.cclProxy.createIconTransformer(iconRegister.registerIcon("logisticspipes:" + "pipes/PipeModel-inactive"));
			LogisticsNewRenderPipe.innerBoxTexture = SimpleServiceLocator.cclProxy.createIconTransformer(iconRegister.registerIcon("logisticspipes:" + "pipes/InnerBox"));
			LogisticsNewRenderPipe.glassCenterTexture = SimpleServiceLocator.cclProxy.createIconTransformer(iconRegister.registerIcon("logisticspipes:" + "pipes/Glass_Texture_Center"));
			LogisticsNewRenderPipe.statusTexture = SimpleServiceLocator.cclProxy.createIconTransformer(iconRegister.registerIcon("logisticspipes:" + "pipes/PipeModel-status"));
			LogisticsNewRenderPipe.statusBCTexture = SimpleServiceLocator.cclProxy.createIconTransformer(iconRegister.registerIcon("logisticspipes:" + "pipes/PipeModel-status-BC"));
		} else {
			LogisticsNewRenderPipe.basicPipeTexture.update(iconRegister.registerIcon("logisticspipes:" + "pipes/PipeModel"));
			LogisticsNewRenderPipe.inactiveTexture.update(iconRegister.registerIcon("logisticspipes:" + "pipes/PipeModel-inactive"));
			LogisticsNewRenderPipe.innerBoxTexture.update(iconRegister.registerIcon("logisticspipes:" + "pipes/InnerBox"));
			LogisticsNewRenderPipe.glassCenterTexture.update(iconRegister.registerIcon("logisticspipes:" + "pipes/Glass_Texture_Center"));
			LogisticsNewRenderPipe.statusTexture.update(iconRegister.registerIcon("logisticspipes:" + "pipes/PipeModel-status"));
			LogisticsNewRenderPipe.statusBCTexture.update(iconRegister.registerIcon("logisticspipes:" + "pipes/PipeModel-status-BC"));
		}
	}

	private PlayerConfig config = LogisticsPipes.getClientPlayerConfig();

	public void renderTileEntityAt(LogisticsTileGenericPipe pipeTile, double x, double y, double z, float partialTickTime, double distance) {
		if (pipeTile.pipe instanceof PipeBlockRequestTable) {
			return;
		}
		if (pipeTile.pipe == null) {
			return;
		}
		PipeRenderState renderState = pipeTile.renderState;

		if (renderState.renderList != null && renderState.renderList.isInvalid()) {
			renderState.renderList = null;
		}

		if (renderState.renderList == null) {
			renderState.renderList = SimpleServiceLocator.renderListHandler.getNewRenderList();
		}

		if (distance > config.getRenderPipeDistance() * config.getRenderPipeDistance()) {
			if (config.isUseFallbackRenderer()) {
				renderState.forceRenderOldPipe = true;
			}
		} else {
			renderState.forceRenderOldPipe = false;
			boolean recalculateList = false;
			if (renderState.cachedRenderer == null) {
				List<RenderEntry> objectsToRender = new ArrayList<>();

				if (pipeTile.pipe != null && pipeTile.pipe.actAsNormalPipe()) {
					fillObjectsToRenderList(objectsToRender, pipeTile, renderState);
				}
				if (pipeTile.pipe != null && pipeTile.pipe.getSpecialRenderer() != null) {
					pipeTile.pipe.getSpecialRenderer().renderToList(pipeTile.pipe, objectsToRender);
				}

				renderState.cachedRenderer = objectsToRender;
				recalculateList = true;
			}
			renderList(pipeTile, x, y, z, renderState.renderList, renderState.cachedRenderer, recalculateList);
		}
	}

	private void renderList(TileEntity subTile, double x, double y, double z, GLRenderList renderList, List<RenderEntry> cachedRenderer, boolean recalculateList) {
		if (!renderList.isFilled() || recalculateList) {
			ResourceLocation oldTexture = null;
			renderList.startListCompile();


			SimpleServiceLocator.cclProxy.getRenderState().reset();
			SimpleServiceLocator.cclProxy.getRenderState().setUseNormals(true);
			SimpleServiceLocator.cclProxy.getRenderState().setAlphaOverride(0xff);

			int brightness = new DoubleCoordinates(subTile).getBlock(subTile.getWorld()).getMixedBrightnessForBlock(subTile.getWorld(), subTile.xCoord, subTile.yCoord, subTile.zCoord);
			SimpleServiceLocator.cclProxy.getRenderState().setBrightness(brightness);
			boolean tesselating = false;

			for (RenderEntry model : cachedRenderer) {
				ResourceLocation texture = model.getTexture();
				if (texture == null) {
					throw new NullPointerException();
				}
				if (texture != oldTexture || oldTexture == null) {
					if (tesselating) {
						SimpleServiceLocator.cclProxy.getRenderState().draw();
						tesselating = false;
					}
					oldTexture = texture;
					Minecraft.getMinecraft().getTextureManager().bindTexture(texture);

					SimpleServiceLocator.cclProxy.getRenderState().reset();
					SimpleServiceLocator.cclProxy.getRenderState().setUseNormals(true);
					SimpleServiceLocator.cclProxy.getRenderState().setAlphaOverride(0xff);
					SimpleServiceLocator.cclProxy.getRenderState().setBrightness(brightness);

					SimpleServiceLocator.cclProxy.getRenderState().startDrawing();
					tesselating = true;
				}
				model.getModel().render(model.getOperations());
			}
			if (tesselating) {
				SimpleServiceLocator.cclProxy.getRenderState().setAlphaOverride(0xff);
				SimpleServiceLocator.cclProxy.getRenderState().draw();
			}
			renderList.stopCompile();
		}
		if (renderList != null) {
			GL11.glPushMatrix();
			GL11.glTranslated(x, y, z);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ZERO);
			renderList.render();
			GL11.glDisable(GL11.GL_BLEND);
			GL11.glPopMatrix();
		}
	}

	private void fillObjectsToRenderList(List<RenderEntry> objectsToRender, LogisticsTileGenericPipe pipeTile, PipeRenderState renderState) {
		List<Edge> edgesToRender = new ArrayList<>(Arrays.asList(Edge.values()));
		Map<Corner, Integer> connectionAtCorner = new HashMap<>();
		List<PipeMount> mountCanidates = new ArrayList<>(Arrays.asList(PipeMount.values()));

		int connectionCount = 0;

		for (EnumFacing dir : EnumFacing.VALUES) {
			if (renderState.pipeConnectionMatrix.isConnected(dir) || pipeTile.pipe.hasSpecialPipeEndAt(dir)) {
				connectionCount++;
				if (renderState.pipeConnectionMatrix.isBCConnected(dir) || renderState.pipeConnectionMatrix.isTDConnected(dir)) {
					I3DOperation[] texture = new I3DOperation[] { LogisticsNewRenderPipe.basicPipeTexture };
					if (renderState.textureMatrix.isRouted()) {
						if (renderState.textureMatrix.isRoutedInDir(dir)) {
							if (renderState.textureMatrix.isSubPowerInDir(dir)) {
								texture = new I3DOperation[] { new LPUVTransformationList(new LPUVTranslation(0, +23F / 100), LogisticsNewRenderPipe.statusBCTexture) };
							} else {
								texture = new I3DOperation[] { LogisticsNewRenderPipe.statusBCTexture };
							}
						} else {
							texture = new I3DOperation[] { new LPUVTransformationList(new LPUVTranslation(0, -23F / 100), LogisticsNewRenderPipe.statusBCTexture) };
						}
					}
					for (IModel3D model : LogisticsNewRenderPipe.sideBC.get(dir)) {
						objectsToRender.add(new RenderEntry(model, texture));
					}
				} else if (!pipeTile.pipe.hasSpecialPipeEndAt(dir)) {
					I3DOperation[] texture = new I3DOperation[] { LogisticsNewRenderPipe.basicPipeTexture };
					if (renderState.textureMatrix.isRouted()) {
						if (renderState.textureMatrix.isRoutedInDir(dir)) {
							if (renderState.textureMatrix.isSubPowerInDir(dir)) {
								texture = new I3DOperation[] { new LPUVTransformationList(new LPUVTranslation(-2.5F / 10, 0), LogisticsNewRenderPipe.statusTexture) };
							} else {
								texture = new I3DOperation[] { LogisticsNewRenderPipe.statusTexture };
							}
						} else {
							if (renderState.textureMatrix.isHasPowerUpgrade()) {
								if (renderState.textureMatrix.getPointedOrientation() == dir) {
									texture = new I3DOperation[] { new LPUVTransformationList(new LPUVTranslation(+2.5F / 10, 0), LogisticsNewRenderPipe.statusTexture) };
								} else {
									texture = new I3DOperation[] { new LPUVTransformationList(new LPUVTranslation(-2.5F / 10, 37F / 100), LogisticsNewRenderPipe.statusTexture) };
								}
							} else {
								if (renderState.textureMatrix.getPointedOrientation() == dir) {
									texture = new I3DOperation[] { new LPUVTransformationList(new LPUVTranslation(+2.5F / 10, 37F / 100), LogisticsNewRenderPipe.statusTexture) };
								} else {
									texture = new I3DOperation[] { new LPUVTransformationList(new LPUVTranslation(0, 37F / 100), LogisticsNewRenderPipe.statusTexture) };
								}
							}
						}
					}
					for (IModel3D model : LogisticsNewRenderPipe.sideNormal.get(dir)) {
						DoubleCoordinates coords = CoordinateUtils.add(new DoubleCoordinates((TileEntity) pipeTile), dir);
						Block block = coords.getBlock(pipeTile.getWorld());
						double[] bounds = { block.getBlockBoundsMinY(), block.getBlockBoundsMinZ(), block.getBlockBoundsMinX(), block.getBlockBoundsMaxY(), block.getBlockBoundsMaxZ(), block.getBlockBoundsMaxX() };
						if(SimpleServiceLocator.enderIOProxy.isItemConduit(coords.getTileEntity(pipeTile.getWorld()), dir.getOpposite()) || SimpleServiceLocator.enderIOProxy.isFluidConduit(coords.getTileEntity(pipeTile.getWorld()), dir.getOpposite())) {
							bounds = new double[]{0.0249D, 0.0249D, 0.0249D, 0.9751D, 0.9751D, 0.9751D};
						}
						double bound = bounds[dir.ordinal() / 2 + (dir.ordinal() % 2 == 0 ? 3 : 0)];
						ScaleObject key = new ScaleObject(model, bound);
						IModel3D model2 = LogisticsNewRenderPipe.scaleMap.get(key);
						if (model2 == null) {
							model2 = model.copy();
							IVec3 min = model2.bounds().min();
							model2.apply(new LPTranslation(min).inverse());
							double toAdd = 1;
							if (dir.ordinal() % 2 == 1) {
								toAdd = 1 + (bound / LPConstants.PIPE_MIN_POS);
								model2.apply(new LPScale(dir.offsetX != 0 ? toAdd : 1, dir.offsetY != 0 ? toAdd : 1, dir.offsetZ != 0 ? toAdd : 1));
							} else {
								bound = 1 - bound;
								toAdd = 1 + (bound / LPConstants.PIPE_MIN_POS);
								model2.apply(new LPScale(dir.offsetX != 0 ? toAdd : 1, dir.offsetY != 0 ? toAdd : 1, dir.offsetZ != 0 ? toAdd : 1));
								model2.apply(new LPTranslation(dir.offsetX * bound, dir.offsetY * bound, dir.offsetZ * bound));
							}
							model2.apply(new LPTranslation(min));
							LogisticsNewRenderPipe.scaleMap.put(key, model2);
						}
						objectsToRender.add(new RenderEntry(model2, texture));
					}
				}
				for (Edge edge : Edge.values()) {
					if (edge.part1 == dir || edge.part2 == dir) {
						edgesToRender.remove(edge);
						for (PipeMount mount : PipeMount.values()) {
							if ((mount.dir == edge.part1 && mount.side == edge.part2) || (mount.dir == edge.part2 && mount.side == edge.part1)) {
								mountCanidates.remove(mount);
							}
						}
					}
				}
				for (Corner corner : Corner.values()) {
					if (corner.ew.dir == dir || corner.ns.dir == dir || corner.ud.dir == dir) {
						if (!connectionAtCorner.containsKey(corner)) {
							connectionAtCorner.put(corner, 1);
						} else {
							connectionAtCorner.put(corner, connectionAtCorner.get(corner) + 1);
						}
					}
				}
			}
		}

		for (Corner corner : Corner.values()) {
			IIconTransformation cornerTexture = LogisticsNewRenderPipe.basicPipeTexture;
			if (!renderState.textureMatrix.isHasPower() && renderState.textureMatrix.isRouted()) {
				cornerTexture = LogisticsNewRenderPipe.inactiveTexture;
			} else if (!renderState.textureMatrix.isRouted() && connectionCount > 2) {
				cornerTexture = LogisticsNewRenderPipe.inactiveTexture;
			}
			int count = connectionAtCorner.containsKey(corner) ? connectionAtCorner.get(corner) : 0;
			if (count == 0) {
				for (IModel3D model : LogisticsNewRenderPipe.corners_M.get(corner)) {
					objectsToRender.add(new RenderEntry(model, new I3DOperation[] { cornerTexture }));
				}
			} else if (count == 1) {
				for (PipeTurnCorner turn : PipeTurnCorner.values()) {
					if (turn.corner != corner) {
						continue;
					}
					if (renderState.pipeConnectionMatrix.isConnected(turn.getPointer()) || pipeTile.pipe.hasSpecialPipeEndAt(turn.getPointer())) {
						objectsToRender.add(new RenderEntry(LogisticsNewRenderPipe.spacers.get(turn), new I3DOperation[] { cornerTexture }));
						break;
					}
				}
			} else if (count == 2) {
				for (PipeTurnCorner turn : PipeTurnCorner.values()) {
					if (turn.corner != corner) {
						continue;
					}
					if (!renderState.pipeConnectionMatrix.isConnected(turn.getPointer()) || pipeTile.pipe.hasSpecialPipeEndAt(turn.getPointer())) {
						objectsToRender.add(new RenderEntry(LogisticsNewRenderPipe.corners_I.get(turn), new I3DOperation[] { cornerTexture }));
						break;
					}
				}
			} else if (count == 3) {
				for (IModel3D model : LogisticsNewRenderPipe.corners_I3.get(corner)) {
					objectsToRender.add(new RenderEntry(model, new I3DOperation[] { cornerTexture }));
				}
			}
		}

		objectsToRender.addAll(edgesToRender.stream()
				.map(edge -> new RenderEntry(LogisticsNewRenderPipe.edges.get(edge), new I3DOperation[]{LogisticsNewRenderPipe.basicPipeTexture})).collect(Collectors.toList()));

		for (int i = 0; i < 6; i += 2) {
			EnumFacing dir = EnumFacing.getOrientation(i);
			List<EnumFacing> list = new ArrayList<>(Arrays.asList(EnumFacing.VALUES));
			list.remove(dir);
			list.remove(dir.getOpposite());
			if (renderState.pipeConnectionMatrix.isConnected(dir) && renderState.pipeConnectionMatrix.isConnected(dir.getOpposite())) {
				boolean found = false;
				for (EnumFacing dir2 : list) {
					if (renderState.pipeConnectionMatrix.isConnected(dir2)) {
						found = true;
						break;
					}
				}
				if (!found) {
					switch (dir) {
						case DOWN:
							objectsToRender.add(new RenderEntry(LogisticsNewRenderPipe.supports.get(PipeSupport.EAST_SIDE), new I3DOperation[] { LogisticsNewRenderPipe.basicPipeTexture }));
							objectsToRender.add(new RenderEntry(LogisticsNewRenderPipe.supports.get(PipeSupport.WEST_SIDE), new I3DOperation[] { LogisticsNewRenderPipe.basicPipeTexture }));
							objectsToRender.add(new RenderEntry(LogisticsNewRenderPipe.supports.get(PipeSupport.NORTH_SIDE), new I3DOperation[] { LogisticsNewRenderPipe.basicPipeTexture }));
							objectsToRender.add(new RenderEntry(LogisticsNewRenderPipe.supports.get(PipeSupport.SOUTH_SIDE), new I3DOperation[] { LogisticsNewRenderPipe.basicPipeTexture }));
							break;
						case NORTH:
							objectsToRender.add(new RenderEntry(LogisticsNewRenderPipe.supports.get(PipeSupport.EAST_UP), new I3DOperation[] { LogisticsNewRenderPipe.basicPipeTexture }));
							objectsToRender.add(new RenderEntry(LogisticsNewRenderPipe.supports.get(PipeSupport.WEST_UP), new I3DOperation[] { LogisticsNewRenderPipe.basicPipeTexture }));
							objectsToRender.add(new RenderEntry(LogisticsNewRenderPipe.supports.get(PipeSupport.UP_SIDE), new I3DOperation[] { LogisticsNewRenderPipe.basicPipeTexture }));
							objectsToRender.add(new RenderEntry(LogisticsNewRenderPipe.supports.get(PipeSupport.DOWN_SIDE), new I3DOperation[] { LogisticsNewRenderPipe.basicPipeTexture }));
							break;
						case WEST:
							objectsToRender.add(new RenderEntry(LogisticsNewRenderPipe.supports.get(PipeSupport.UP_UP), new I3DOperation[] { LogisticsNewRenderPipe.basicPipeTexture }));
							objectsToRender.add(new RenderEntry(LogisticsNewRenderPipe.supports.get(PipeSupport.DOWN_UP), new I3DOperation[] { LogisticsNewRenderPipe.basicPipeTexture }));
							objectsToRender.add(new RenderEntry(LogisticsNewRenderPipe.supports.get(PipeSupport.NORTH_UP), new I3DOperation[] { LogisticsNewRenderPipe.basicPipeTexture }));
							objectsToRender.add(new RenderEntry(LogisticsNewRenderPipe.supports.get(PipeSupport.SOUTH_UP), new I3DOperation[] { LogisticsNewRenderPipe.basicPipeTexture }));
							break;
						default:
							break;
					}
				}
			}
		}

		boolean[] solidSides = new boolean[6];
		for (EnumFacing dir : EnumFacing.VALUES) {
			DoubleCoordinates pos = CoordinateUtils.add(new DoubleCoordinates((TileEntity) pipeTile), dir);
			Block blockSide = pos.getBlock(pipeTile.getWorld());
			if (blockSide == null || !blockSide.isSideSolid(pipeTile.getWorld(), pos.getXInt(), pos.getYInt(), pos.getZInt(), dir.getOpposite()) || renderState.pipeConnectionMatrix.isConnected(dir)) {
				Iterator<PipeMount> iter = mountCanidates.iterator();
				while (iter.hasNext()) {
					PipeMount mount = iter.next();
					if (mount.dir == dir) {
						iter.remove();
					}
				}
			} else {
				solidSides[dir.ordinal()] = true;
			}
		}

		if (!mountCanidates.isEmpty()) {
			if (solidSides[EnumFacing.DOWN.ordinal()]) {
				findOponentOnSameSide(mountCanidates, EnumFacing.DOWN);
			} else if (solidSides[EnumFacing.UP.ordinal()]) {
				findOponentOnSameSide(mountCanidates, EnumFacing.UP);
			} else {
				removeFromSide(mountCanidates, EnumFacing.DOWN);
				removeFromSide(mountCanidates, EnumFacing.UP);
				if (mountCanidates.size() > 2) {
					removeIfHasOponentSide(mountCanidates);
				}
				if (mountCanidates.size() > 2) {
					removeIfHasConnectedSide(mountCanidates);
				}
				if (mountCanidates.size() > 2) {
					findOponentOnSameSide(mountCanidates, mountCanidates.get(0).dir);
				}
			}

			if (LPConstants.DEBUG && mountCanidates.size() > 2) {
				new RuntimeException("Trying to render " + mountCanidates.size() + " Mounts").printStackTrace();
			}

			objectsToRender.addAll(mountCanidates.stream()
					.map(mount -> new RenderEntry(LogisticsNewRenderPipe.mounts.get(mount), new I3DOperation[]{LogisticsNewRenderPipe.basicPipeTexture}))
					.collect(Collectors.toList()));
		}

		for (EnumFacing dir : EnumFacing.VALUES) {
			if (!renderState.pipeConnectionMatrix.isConnected(dir)) {
				for (IModel3D model : LogisticsNewRenderPipe.texturePlate_Outer.get(dir)) {
					IIconTransformation icon = Textures.LPnewPipeIconProvider.getIcon(renderState.textureMatrix.getTextureIndex());
					if (icon != null) {
						objectsToRender.add(new RenderEntry(model, new I3DOperation[] { icon }));
					}
				}
			}
		}
		if (renderState.textureMatrix.isFluid()) {
			for (EnumFacing dir : EnumFacing.VALUES) {
				if (!renderState.pipeConnectionMatrix.isConnected(dir)) {
					objectsToRender.addAll(LogisticsNewRenderPipe.texturePlate_Inner.get(dir).stream()
							.map(model -> new RenderEntry(model, new I3DOperation[]{LogisticsNewRenderPipe.glassCenterTexture}))
							.collect(Collectors.toList()));
				} else {
					if (!renderState.textureMatrix.isRoutedInDir(dir)) {
						objectsToRender.addAll(LogisticsNewRenderPipe.sideTexturePlate.get(dir).getValue1().stream()
								.map(model -> new RenderEntry(model, new I3DOperation[]{LogisticsNewRenderPipe.basicPipeTexture}))
								.collect(Collectors.toList()));
					}
				}
			}
		}
	}

	private void findOponentOnSameSide(List<PipeMount> mountCanidates, EnumFacing dir) {
		boolean sides[] = new boolean[6];
		Iterator<PipeMount> iter = mountCanidates.iterator();
		while (iter.hasNext()) {
			PipeMount mount = iter.next();
			if (mount.dir != dir) {
				iter.remove();
			} else {
				sides[mount.side.ordinal()] = true;
			}
		}
		if (mountCanidates.size() <= 2) {
			return;
		}
		List<EnumFacing> keep = new ArrayList<>();
		if (sides[2] && sides[3]) {
			keep.add(EnumFacing.NORTH);
			keep.add(EnumFacing.SOUTH);
		} else if (sides[4] && sides[5]) {
			keep.add(EnumFacing.EAST);
			keep.add(EnumFacing.WEST);
		} else if (sides[0] && sides[1]) {
			keep.add(EnumFacing.UP);
			keep.add(EnumFacing.DOWN);
		}
		iter = mountCanidates.iterator();
		while (iter.hasNext()) {
			PipeMount mount = iter.next();
			if (!keep.contains(mount.side)) {
				iter.remove();
			}
		}
	}

	private void removeFromSide(List<PipeMount> mountCanidates, EnumFacing dir) {
		Iterator<PipeMount> iter = mountCanidates.iterator();
		while (iter.hasNext()) {
			PipeMount mount = iter.next();
			if (mount.dir == dir) {
				iter.remove();
			}
		}
	}

	private void reduceToOnePerSide(List<PipeMount> mountCanidates, EnumFacing dir, EnumFacing pref) {
		boolean found = false;
		for (PipeMount mount : mountCanidates) {
			if (mount.dir != dir) {
				continue;
			}
			if (mount.side == pref) {
				found = true;
			}
		}
		if (!found) {
			reduceToOnePerSide(mountCanidates, dir);
		} else {
			Iterator<PipeMount> iter = mountCanidates.iterator();
			while (iter.hasNext()) {
				PipeMount mount = iter.next();
				if (mount.dir != dir) {
					continue;
				}
				if (mount.side != pref) {
					iter.remove();
				}
			}
		}
	}

	private void reduceToOnePerSide(List<PipeMount> mountCanidates, EnumFacing dir) {
		boolean found = false;
		Iterator<PipeMount> iter = mountCanidates.iterator();
		while (iter.hasNext()) {
			PipeMount mount = iter.next();
			if (mount.dir != dir) {
				continue;
			}
			if (found) {
				iter.remove();
			} else {
				found = true;
			}
		}
	}

	private void removeIfHasOponentSide(List<PipeMount> mountCanidates) {
		boolean sides[] = new boolean[6];
		for (PipeMount mount : mountCanidates) {
			sides[mount.dir.ordinal()] = true;
		}
		if (sides[2] && sides[3]) {
			removeFromSide(mountCanidates, EnumFacing.EAST);
			removeFromSide(mountCanidates, EnumFacing.WEST);
			reduceToOnePerSide(mountCanidates, EnumFacing.NORTH);
			reduceToOnePerSide(mountCanidates, EnumFacing.SOUTH);
		} else if (sides[4] && sides[5]) {
			removeFromSide(mountCanidates, EnumFacing.NORTH);
			removeFromSide(mountCanidates, EnumFacing.SOUTH);
			reduceToOnePerSide(mountCanidates, EnumFacing.EAST);
			reduceToOnePerSide(mountCanidates, EnumFacing.WEST);
		}
	}

	private void removeIfHasConnectedSide(List<PipeMount> mountCanidates) {
		boolean sides[] = new boolean[6];
		for (PipeMount mount : mountCanidates) {
			sides[mount.dir.ordinal()] = true;
		}
		for (int i = 2; i < 6; i++) {
			EnumFacing dir = EnumFacing.getOrientation(i);
			EnumFacing rot = dir.getRotation(EnumFacing.UP);
			if (sides[dir.ordinal()] && sides[rot.ordinal()]) {
				reduceToOnePerSide(mountCanidates, dir, dir.getRotation(EnumFacing.DOWN));
				reduceToOnePerSide(mountCanidates, rot, rot.getRotation(EnumFacing.UP));
			}
		}
	}

	public static void renderDestruction(CoreUnroutedPipe pipe, World worldObj, int x, int y, int z, EffectRenderer effectRenderer) {
		if (pipe.container != null && pipe.container.renderState != null && pipe.container.renderState.cachedRenderer != null) {
			for (RenderEntry entry : pipe.container.renderState.cachedRenderer) {
				IModel3D model = entry.getModel().twoFacedCopy();
				IBounds bounds = model.bounds();
				double xMid = (bounds.min().x() + bounds.max().x()) / 2;
				double yMid = (bounds.min().y() + bounds.max().y()) / 2;
				double zMid = (bounds.min().z() + bounds.max().z()) / 2;
				model.apply(new LPTranslation(-xMid, -yMid, -zMid));
				effectRenderer.addEffect(new EntityModelFX(worldObj, x + xMid, y + yMid, z + zMid, model, entry.getOperations(), entry.getTexture()));
			}
		}
	}

	public static void renderBoxWithDir(EnumFacing dir) {
		List<RenderEntry> objectsToRender = new ArrayList<>();
		List<Edge> edgesToRender = new ArrayList<>(Arrays.asList(Edge.values()));
		Map<Corner, Integer> connectionAtCorner = new HashMap<>();

		for (Edge edge : Edge.values()) {
			if (edge.part1 == dir || edge.part2 == dir) {
				edgesToRender.remove(edge);
			}
		}
		for (Corner corner : Corner.values()) {
			if (corner.ew.dir == dir || corner.ns.dir == dir || corner.ud.dir == dir) {
				if (!connectionAtCorner.containsKey(corner)) {
					connectionAtCorner.put(corner, 1);
				} else {
					connectionAtCorner.put(corner, connectionAtCorner.get(corner) + 1);
				}
			}
		}
		for (Corner corner : Corner.values()) {
			IIconTransformation cornerTexture = LogisticsNewRenderPipe.basicPipeTexture;
			int count = connectionAtCorner.containsKey(corner) ? connectionAtCorner.get(corner) : 0;
			if (count == 0) {
				objectsToRender.addAll(LogisticsNewRenderPipe.corners_M.get(corner).stream()
						.map(model -> new RenderEntry(model, new I3DOperation[]{cornerTexture}))
						.collect(Collectors.toList()));
			} else if (count == 1) {
				for (PipeTurnCorner turn : PipeTurnCorner.values()) {
					if (turn.corner != corner) {
						continue;
					}
					if (turn.getPointer() == dir) {
						objectsToRender.add(new RenderEntry(LogisticsNewRenderPipe.spacers.get(turn), new I3DOperation[] { cornerTexture }));
						break;
					}
				}
			} else if (count == 2) {
				for (PipeTurnCorner turn : PipeTurnCorner.values()) {
					if (turn.corner != corner) {
						continue;
					}
					if (turn.getPointer() == dir) {
						objectsToRender.add(new RenderEntry(LogisticsNewRenderPipe.corners_I.get(turn), new I3DOperation[] { cornerTexture }));
						break;
					}
				}
			} else if (count == 3) {
				objectsToRender.addAll(LogisticsNewRenderPipe.corners_I3.get(corner).stream()
						.map(model -> new RenderEntry(model, new I3DOperation[]{cornerTexture}))
						.collect(Collectors.toList()));
			}
		}

		objectsToRender.addAll(edgesToRender.stream()
				.map(edge -> new RenderEntry(LogisticsNewRenderPipe.edges.get(edge), new I3DOperation[]{LogisticsNewRenderPipe.basicPipeTexture}))
				.collect(Collectors.toList()));
		for (RenderEntry model : objectsToRender) {
			model.getModel().render(model.getOperations());
		}
	}

	@Override
	public void renderHighlight(ITubeOrientation orientation) {
		highlight.render(new I3DOperation[] { LPColourMultiplier.instance(LogisticsPipes.LogisticsPipeBlock.getBlockColor() << 8 | 0xFF) });
	}
	*/
}
