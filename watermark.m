orig = imread('walle.jpg');
dim = size(orig);
pink = [225, 197, 218];
%green = [197, 225, 205];
green = [173, 218, 218];
for row = (47:32:dim(1,1)-47),
    for col = (47:32:dim(1,2)-47),
%         orig(row, col, :) = green;
%         orig(row+1, col, :) = pink;
%         orig(row, col+1, :) = pink;
%         orig(row+1, col+1, :) = green;
        for r_inc = (0:2:6),
            for c_inc = (0:2:6),
                orig(row+r_inc, col+c_inc, :) = green;
            end
        end
    end
end

imwrite(orig, 'test.png', 'png');
imshow(orig);