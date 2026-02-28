package enums;

public enum Direction
{
	Up
	{
		@Override
		public Direction inverted()
		{
			return Down;
		}
	},
	Down
	{
		@Override
		public Direction inverted()
		{
			return Up;
		}
	},
	Left
	{
		@Override
		public Direction inverted()
		{
			return Right;
		}
	},
	Right
	{
		@Override
		public Direction inverted()
		{
			return Left;
		}
	};
	
	public abstract Direction inverted();
}
