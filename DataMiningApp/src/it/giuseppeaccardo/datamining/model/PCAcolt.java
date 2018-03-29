	package it.giuseppeaccardo.datamining.model;    
	
	import cern.colt.matrix.DoubleMatrix1D;
    import cern.colt.matrix.DoubleMatrix2D;
    import cern.colt.matrix.doublealgo.Statistic;
    import cern.colt.matrix.impl.DenseDoubleMatrix2D;
    import cern.colt.matrix.impl.SparseDoubleMatrix2D;
    import cern.colt.matrix.linalg.Algebra;
    import cern.colt.matrix.linalg.EigenvalueDecomposition;
    import hep.aida.bin.DynamicBin1D;
    /**
     * <h1>PCA Colt!</h1>
     * Classe utile per la effettura la PCA (Analisi delle Componenti Principali) che è una tecnica per la semplificazione 
     * dei dati utilizzata nell'ambito della statistica multivariata. Lo scopo primario di questa tecnica è la riduzione di 
     * un numero variabili (rappresentanti altrettante caratteristiche del fenomeno analizzato) in alcune variabili latenti
     * (feature reduction). In particolare, il numero delle variabili saranno ridotte a 3 in modo da poter plottare in un grafico
     * 3D. Ciò avviene tramite una trasformazione lineare delle variabili che proietta quelle originarie in un nuovo sistema 
     * cartesiano nel quale la nuova variabile con la maggiore varianza viene proiettata sul primo asse, la variabile nuova, 
     * seconda per dimensione della varianza, sul secondo asse e così via.<br><br>
     * Tale classe utilizza la COLT, una libreria molto utilizzata in ambito scientifico in java.
     * @author Giuseppe Accardo
     * @version 1.0
     * @since   14-02-2017
     * @see DataMining
     * @see Dataset
     */
    public class PCAcolt 
    {
        /**
         * Effettua la riduzione della matrice in uno spazio a 3 dimensioni (features), cioè una matrice
         * con 3 colonne
         * @param data Tabella su cui effettura la riduzione delle features.
         * @return reduxeMatrix Matrice ridotta su uno spazio a 3 dimensioni
         */
        // to show matrix creation, it does not make much sense to calculate PCA on random data
        public static double[][] pcaReduxMatrix(double [][] data) 
        {
        	/* Applcia la trasformazione PCA */
            DoubleMatrix2D matrix = new DenseDoubleMatrix2D(data);
            DoubleMatrix2D pm = pcaTransform(matrix);

            // print the first two dimensions of the transformed matrix - they capture most of the variance of the original data
            System.out.println("Applico PCA per ridurre matrice in 3D\n"+pm.viewPart(0, 0, pm.rows(), 3).toString());
			return pm.toArray();
        }

        /** Returns a matrix in the space of principal components, take the first n columns  
         * @param matrix matrix
         * @return matrix2D matrix2D*/
        public static DoubleMatrix2D pcaTransform(DoubleMatrix2D matrix) {
            DoubleMatrix2D zScoresMatrix = toZScores(matrix);
            final DoubleMatrix2D covarianceMatrix = Statistic.covariance(zScoresMatrix);

            // compute eigenvalues and eigenvectors of the covariance matrix (flip needed since it is sorted by ascending).
            final EigenvalueDecomposition decomp = new EigenvalueDecomposition(covarianceMatrix);

            // Columns of Vs are eigenvectors = principal components = base of the new space; ordered by decreasing variance
            final DoubleMatrix2D Vs = decomp.getV().viewColumnFlip(); 

            // eigenvalues: ev(i) / sum(ev) is the percentage of variance captured by i-th column of Vs
            // final DoubleMatrix1D ev = decomp.getRealEigenvalues().viewFlip();

            // project the original matrix to the pca space
            return Algebra.DEFAULT.mult(zScoresMatrix, Vs);
        }


        /**
         * Converts matrix to a matrix of z-scores (by columns)
         * @param matrix matrix2D
         * @return zMatrix zmatrix
         */
        public static DoubleMatrix2D toZScores(final DoubleMatrix2D matrix) {
            final DoubleMatrix2D zMatrix = new SparseDoubleMatrix2D(matrix.rows(), matrix.columns());
            for (int c = 0; c < matrix.columns(); c++) {
                final DoubleMatrix1D column = matrix.viewColumn(c);
                final DynamicBin1D bin = Statistic.bin(column);

                if (bin.standardDeviation() == 0) {   // use epsilon
                    for (int r = 0; r < matrix.rows(); r++) {
                        zMatrix.set(r, c, 0.0);
                    }
                } else {
                    for (int r = 0; r < matrix.rows(); r++) {
                        double zScore = (column.get(r) - bin.mean()) / bin.standardDeviation();
                        zMatrix.set(r, c, zScore);
                    }
                }
            }

            return zMatrix;
        }
    }